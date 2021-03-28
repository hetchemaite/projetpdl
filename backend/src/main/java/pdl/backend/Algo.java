package pdl.backend;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
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
			rgbToHsv(valR.get(),valG.get(),valB.get(),hsv);
			histogramme[(int) hsv[1]]++;
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
			rgbToHsv(valR.get(),valG.get(),valB.get(),hsv);
			int s=(histogrammeCumule[(int) hsv[1]]*255/histogrammeCumule[255]);
			
			hsvToRgb(hsv[0],s,hsv[2],rgb);
			valR.set(rgb[0]);
			valG.set(rgb[1]);
			valB.set(rgb[2]);
		}
	}

	public static void Teinte(Img<UnsignedByteType> img, int teinte) {
		final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(img, 2, 0);
        final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(img, 2, 1);
        final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(img, 2, 2);
        final Cursor<UnsignedByteType> cR = inputR.cursor();
        final Cursor<UnsignedByteType> cG = inputG.cursor();
        final Cursor<UnsignedByteType> cB = inputB.cursor();

		float[] hsv={0,0,0};
		int[] rgb={0,0,0};
		while(cR.hasNext() && cG.hasNext() && cB.hasNext()){
			cR.fwd();
            cG.fwd();
            cB.fwd();
			final UnsignedByteType valR = cR.get();
			final UnsignedByteType valG = cG.get();
			final UnsignedByteType valB = cB.get();
			rgbToHsv(valR.get(),valG.get(),valB.get(),hsv);			
			hsvToRgb(teinte, hsv[1],hsv[2],rgb);
			valR.set(rgb[0]);
			valG.set(rgb[1]);
			valB.set(rgb[2]);
		}
	}
	
    public static  void rgbToHsv(int r, int g, int b, float[] hsv){
		float h,s,v;
		int max=Math.max(Math.max(r,g),b);
		int min=Math.min(Math.min(r,g),b);
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

    public static void hsvToRgb(float h, float s, float v, int[] rgb){
		float hi=((int)(h/60)) %6;
		float f=h/60 -hi;
		float l = v * (1-s);
		float m = v * (1-f*s);
		float n = v * (1 -(1-f)*s);
		int l_int = (int) l;
		int m_int = (int) m;
		int n_int = (int) n;
		if(hi==0){
			int[] myArray={(int)v,n_int,l_int};
			rgb=myArray;
		}
		if(hi==1){
			int[] myArray={m_int,(int)v,l_int};
			rgb=myArray;
		}
		if(hi==2){
			int[] myArray={l_int,(int)v,n_int};
			rgb=myArray;
		}
		if(hi==3){
			int[] myArray={l_int,m_int,(int)v};
			rgb=myArray;
		}
		if(hi==4){
			int[] myArray={n_int,l_int,(int)v};
			rgb=myArray;
		}
		if(hi==5){
			int[] myArray={(int) v,l_int,m_int};
			rgb=myArray;
	
		}

	}

	public static void meanFilter(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output, int size) {
		
		
		final IntervalView<UnsignedByteType> expandedView = Views.expandMirrorDouble(input, size/2, size/2 );
		final RandomAccess<UnsignedByteType> w = output.randomAccess();
			final int iw = (int) input.max(0);
			final int ih = (int) input.max(1);
			final double[][] filter=new double[size][size];
			for(int a=0; a<size; a++){
				for(int b=0; b<size; b++){
					filter[a][b]=1.0/(size*size);
				}
			}
			for (int x = 0; x <= iw-1; ++x) {
				for (int y = 0; y <= ih-1; ++y) {
					double vR=0;
					double vG=0;
					double vB=0;
					int i=0;
					RandomAccessibleInterval< UnsignedByteType > convolution = Views.interval( expandedView, new long[] { x-size/2, y-size/2 }, new long[]{ x+size/2, y+size/2} );

					final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(convolution, 2, 0);
					final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(convolution, 2, 1);
					final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(convolution, 2, 2);
					final Cursor<UnsignedByteType> cR = inputR.cursor();
					final Cursor<UnsignedByteType> cG = inputG.cursor();
					final Cursor<UnsignedByteType> cB = inputB.cursor();

					while(cR.hasNext() && cG.hasNext() && cB.hasNext()){
						cR.fwd();
						cG.fwd();
						cB.fwd();
						final UnsignedByteType valR = cR.get();
						final UnsignedByteType valG = cG.get();
						final UnsignedByteType valB = cB.get();
						vR=vR+valR.get()*filter[i/size][i%size];
						vG=vG+valG.get()*filter[i/size][i%size];
						vB=vB+valB.get()*filter[i/size][i%size];
						i++;
					}
					w.setPosition(x, 0);
					w.setPosition(y, 1);

					w.setPosition(0, 2);
					final UnsignedByteType valR2=w.get();
					valR2.set((int) vR);
					w.setPosition(1, 2);
					final UnsignedByteType valG2=w.get();
					valG2.set((int) vG);
					w.setPosition(2, 2);
					final UnsignedByteType valB2=w.get();
					valB2.set((int) vB);

				}
			}
	}




}
