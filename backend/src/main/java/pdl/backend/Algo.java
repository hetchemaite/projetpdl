package pdl.backend;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;

import java.util.List;

import io.scif.SCIFIO;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;
import net.imglib2.exception.IncompatibleTypeException;

public class Algo {


    public static void increaseLuminosity(Img<UnsignedByteType> img, int delta) {
		final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(img, 2, 0);
        final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(img, 2, 1);
        final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(img, 2, 2);

        final Cursor<UnsignedByteType> cR = inputR.cursor();
        final Cursor<UnsignedByteType> cG = inputG.cursor();
        final Cursor<UnsignedByteType> cB = inputB.cursor();
	
		while (cR.hasNext() && cG.hasNext() && cB.hasNext()) {
			cR.fwd();
            cG.fwd();
            cB.fwd();

			final UnsignedByteType valR = cR.get();
			final UnsignedByteType valG = cG.get();
			final UnsignedByteType valB = cB.get();
			if(valR.get() + delta > 255) {
				valR.set(255);
			} else if(valR.get()+delta < 0 ) {
				valR.set(0);
			} else {
				valR.set(valR.get()+delta);
			}

			if(valG.get() + delta > 255) {
				valG.set(255);
			} else if(valG.get()+delta < 0 ) {
				valG.set(0);
			} else {
				valG.set(valG.get()+delta);
			}

			if(valB.get() + delta > 255) {
				valB.set(255);
			} else if(valB.get()+delta < 0 ) {
				valB.set(0);
			} else {
				valB.set(valB.get()+delta);
			}
		}

	}

	public static void EgalisationHistogramme(Img<UnsignedByteType> img) {
		final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(img, 2, 0);
        final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(img, 2, 1);
        final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(img, 2, 2);
        final Cursor<UnsignedByteType> cR = inputR.cursor();
        final Cursor<UnsignedByteType> cG = inputG.cursor();
        final Cursor<UnsignedByteType> cB = inputB.cursor();
		final Cursor<UnsignedByteType> cRcopy = cR.copyCursor();
        final Cursor<UnsignedByteType> cGcopy = cG.copyCursor();
        final Cursor<UnsignedByteType> cBcopy = cB.copyCursor();

		int[] histogramme=new int[256];
		int[] histogrammeCumule=new int[256];
		float[] hsv={0,0,0};
		while (cR.hasNext() && cG.hasNext() && cB.hasNext()) {
			cR.fwd();
            cG.fwd();
            cB.fwd();
			final UnsignedByteType valR = cR.get();
			final UnsignedByteType valG = cG.get();
			final UnsignedByteType valB = cB.get();
			rgbToHsv(valR.get(),valG.get(),ValB.get(),hsv);
			h.add(hsv[0]);
			v.add(hsv[2]);
			histogramme[hsv[1]]++;
		}
		for(int i=0; i<256; i++){
			if(i==0){
				histogrammeCumule[0]=histogramme[0];
			}else{
				histogrammeCumule[i]=histogrammeCumule[i-1]+histogramme[i];
			}
		}
		int[] rgb={0,0,0};
		while(cRcopy.hasNext() && cGcopy.hasNext() && cBcopy.hasNext()){
			cRcopy.fwd();
            cGcopy.fwd();
            cBcopy.fwd();
			final UnsignedByteType valR = cR.get();
			final UnsignedByteType valG = cG.get();
			final UnsignedByteType valB = cB.get();
			rgbToHsv(valR.get(),valG.get(),ValB.get(),hsv);
			int s=(histogrammeCumule[hsv[1]]*255/histogrammeCumule[255]);
			
			hsvToRgb(hvs[0],s,hsv[2],rgb);
			valR.set(rgb[0]);
			valG.set(rgb[1]);
			valB.set(rgb[2]);
		}
	}

	
    void rgbToHsv(int r, int g, int b, float[] hsv){
		float h,s,v;
		int max=max(max(r,g),b);
		int min=min(min(r,g),b);
		int Maxmin=max-min;
		if(max==min){
			h=0;
		}else if(max==r){
			h=(60*(g-b)/Maxmin) % 360;
		}else if(max==g){
			h=60*(b-r)/Maxmin +120;
		}else if(max==b){
			h=60*(r-g)/Maxmin + 240;
		}
		if(max==0){
			s=0;
		}else{
			s=1-(min/max);
		}
		v=max;
		hsv[0]=h;
		hsv[1]=s;
		hsv[2]=v;
	}

    void hsvToRgb(float h, float s, float v, int[] rgb){
		float hi=((int)(h/60)) %6;
		float f=h/60 -hi;
		float l = v * (1-s);
		float m = v * (1-f*s);
		float n = v * (1 -(1-f)*s);
		if(hi=0){
			int[] myArray={v,n,l};
		}
		if(hi=1){
			int[] myArray={m,v,l};
		}
		if(hi=2){
			int[] myArray={l,v,n};
		}
		if(hi=3){
			int[] myArray={l,m,v};
		}
		if(hi=4){
			int[] myArray={n,l,v};
		}
		if(hi=5){
			int[] myArray={v,l,m};
		}
		rgb=myArray;
	}

}
