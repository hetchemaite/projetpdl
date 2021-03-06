package pdl.backend;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import java.awt.Color;

import java.util.List;

import io.scif.SCIFIO;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.Dimensions;
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
			Color.RGBtoHSB(valR.get(),valG.get(),valB.get(),hsv);
			histogramme[(int) hsv[1]]++;
		}
		for(int i=0; i<256; i++){
			if(i==0){
				histogrammeCumule[0]=histogramme[0];
			}else{
				histogrammeCumule[i]=histogrammeCumule[i-1]+histogramme[i];
			}
		}
		int rgb;
		while(cRcopy.hasNext() && cGcopy.hasNext() && cBcopy.hasNext()){
			cRcopy.fwd();
            cGcopy.fwd();
            cBcopy.fwd();
			final UnsignedByteType valR = cR.get();
			final UnsignedByteType valG = cG.get();
			final UnsignedByteType valB = cB.get();
			Color.RGBtoHSB(valR.get(),valG.get(),valB.get(),hsv);
			int s=(histogrammeCumule[(int) hsv[1]]*255/histogrammeCumule[255]);
			
			rgb = Color.HSBtoRGB(hsv[0],s, hsv[2]);
			valR.set((rgb>>16)&0xFF);
			valG.set((rgb>>8)&0xFF);
			valB.set(rgb&0xFF);
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
		int rgb;
		while(cR.hasNext() && cG.hasNext() && cB.hasNext()){
			cR.fwd();
            cG.fwd();
            cB.fwd();
			final UnsignedByteType valR = cR.get();
			final UnsignedByteType valG = cG.get();
			final UnsignedByteType valB = cB.get();

			hsv = Color.RGBtoHSB(valR.get(),valG.get(),valB.get(),null);
			rgb = Color.HSBtoRGB(teinte/360.0F, hsv[1], hsv[2]);
			valR.set((rgb>>16)&0xFF);
			valG.set((rgb>>8)&0xFF);
			valB.set(rgb&0xFF);
		}

	}
	public static void convolution(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
	float[][] kernel) {
		int size=kernel.length;
		
		final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(input, 2, 0);
		final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(input, 2, 1);
		final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(input, 2, 2);
		final IntervalView<UnsignedByteType> expandedViewR = Views.expandMirrorDouble(inputR, size/2, size/2 );
		final IntervalView<UnsignedByteType> expandedViewG = Views.expandMirrorDouble(inputG, size/2, size/2 );
		final IntervalView<UnsignedByteType> expandedViewB = Views.expandMirrorDouble(inputB, size/2, size/2 );
		final RandomAccess<UnsignedByteType> r = input.randomAccess();
		final RandomAccess<UnsignedByteType> w = output.randomAccess();
		final int iw = (int) input.max(0);
		final int ih = (int) input.max(1);
		double coeff=0;
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				coeff += kernel[i][j];
			}
		}
		coeff= 1/(coeff);
		for (int x = 1; x <= iw-1; ++x) {
			for (int y = 1; y <= ih-1; ++y) {
			double vR=0;
			double vG=0;
			double vB=0;
			int i=0;
			RandomAccessibleInterval< UnsignedByteType > convolutionR = Views.interval( expandedViewR, new long[] { x-size/2, y-size/2 }, new long[]{ x+size/2, y+size/2} );
			RandomAccessibleInterval< UnsignedByteType > convolutionG = Views.interval( expandedViewG, new long[] { x-size/2, y-size/2 }, new long[]{ x+size/2, y+size/2} );
			RandomAccessibleInterval< UnsignedByteType > convolutionB = Views.interval( expandedViewB, new long[] { x-size/2, y-size/2 }, new long[]{ x+size/2, y+size/2} );

			final Cursor<UnsignedByteType> cR = Views.iterable(convolutionR).cursor();
			final Cursor<UnsignedByteType> cG = Views.iterable(convolutionG).cursor();
			final Cursor<UnsignedByteType> cB = Views.iterable(convolutionB).cursor();

			while(cR.hasNext() && cG.hasNext() && cB.hasNext()){
				cR.fwd();
				cG.fwd();
				cB.fwd();
				final UnsignedByteType valR = cR.get();
				final UnsignedByteType valG = cG.get();
				final UnsignedByteType valB = cB.get();
				vR=vR+valR.get()*kernel[i/size][i%size];
				vG=vG+valG.get()*kernel[i/size][i%size];
				vB=vB+valB.get()*kernel[i/size][i%size];
				i++;
			}
			w.setPosition(x, 0);
			w.setPosition(y, 1);

			w.setPosition(0, 2);
			final UnsignedByteType valR2=w.get();
			vR=vR*coeff;
			valR2.set((int) vR);
			w.setPosition(1, 2);
			final UnsignedByteType valG2=w.get();
			vG=vG*coeff;
			valG2.set((int) vG);
			w.setPosition(2, 2);
			final UnsignedByteType valB2=w.get();
			vB=vB*coeff;
			valB2.set((int) vB);
			}
		}
	}
	public static void meanFilter(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output, int size) {
		final float[][] filter=new float[size][size];
		for(int a=0; a<size; a++){
			for(int b=0; b<size; b++){
				filter[a][b]=1.0F;
			}
		}
	}

	public static float[][] MatrixGauss(int size){
        int P=(size-1)/2;
        float sigma = 4/3;
        float somme=0;
        float[][] matrice=new float[size][size];
        for(int i=-P; i<P+1; i++){
            for(int j=-P; j<P+1; j++){
                matrice[i+P][j+P]=(float) Math.exp(-(i*i + j*j)/(2*sigma*sigma));
                somme+=matrice[i+P][j+P];
            }
        }
        for(int i=-P; i<P+1; i++){
            for(int j=-P; j<P+1; j++){
                matrice[i+P][j+P]/=somme;
            }
        }
        return matrice;
    }

    public static void FiltreGaussien(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output, int size){
        convolution(input, output, MatrixGauss(size));
    }
	

	public static void convertGrey(Img<UnsignedByteType> input) {
        final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(input, 2, 0);
        final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(input, 2, 1);
        final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(input, 2, 2);

        final Cursor<UnsignedByteType> cR = inputR.cursor();
        final Cursor<UnsignedByteType> cG = inputG.cursor();
        final Cursor<UnsignedByteType> cB = inputB.cursor();

        while (cR.hasNext() && cG.hasNext() && cB.hasNext()) {

            cR.fwd();
            cG.fwd();
            cB.fwd();
            double rgb = 0.3 * cR.get().get() + 0.59 * cG.get().get() + 0.11 * cB.get().get();
            int rgb2 = (int) rgb;
            cR.get().set(rgb2);
            cG.get().set(rgb2);
            cB.get().set(rgb2);
        }
    }
	
	
    public static void BorderFilter(Img<UnsignedByteType> input, Img<UnsignedByteType> input2) {
		final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<>(new UnsignedByteType());
		final Dimensions dim = input;
		final Img<UnsignedByteType> outputConvo = factory.create(dim);
        convertGrey(input2);
        int[][] h = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
        int[][] v = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
		
		convolution2(input2, outputConvo, h, v);
		int iw = (int) input.max(0);
		int ih = (int) input.max(1);
		final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(input, 2, 0);
		final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(input, 2, 1);
		final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(input, 2, 2);
		final IntervalView<UnsignedByteType> inputR2 = Views.hyperSlice(outputConvo, 2, 0);
		final Cursor<UnsignedByteType> cR = inputR.cursor();
		final Cursor<UnsignedByteType> cG = inputG.cursor();
		final Cursor<UnsignedByteType> cB = inputB.cursor();

		final Cursor<UnsignedByteType> cR2 = inputR2.cursor();
		while(cR.hasNext() && cG.hasNext() && cB.hasNext() && cR2.hasNext()){
			cR.fwd();
			cG.fwd();
			cB.fwd();
			cR2.fwd();
			final UnsignedByteType valYfinal = cR2.get();
			int yfinal=valYfinal.get();
			final UnsignedByteType valR=cR.get();
			final UnsignedByteType valG=cG.get();
			final UnsignedByteType valB=cB.get();
			int valueR=valR.get();
			int valueG=valG.get();
			int valueB=valB.get();
			double y0=(1.0/3.0)*(valueR+valueG+valueB);
			valR.set((int) (valueR*(yfinal/y0)));
			valG.set((int) (valueG*(yfinal/y0)));
			valB.set((int) (valueB*(yfinal/y0)));
		}

		

    }

	public static void convolution2(final Img<UnsignedByteType> input,final Img<UnsignedByteType> output, int[][] kernel, int[][] kernel2 ) {
		int size=kernel.length;
		final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(input, 2, 0);
		final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(input, 2, 1);
		final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(input, 2, 2);
		final IntervalView<UnsignedByteType> expandedViewR = Views.expandMirrorDouble(inputR, size/2, size/2 );
		final IntervalView<UnsignedByteType> expandedViewG = Views.expandMirrorDouble(inputG, size/2, size/2 );
		final IntervalView<UnsignedByteType> expandedViewB = Views.expandMirrorDouble(inputB, size/2, size/2 );
		final RandomAccess<UnsignedByteType> r = input.randomAccess();
		final RandomAccess<UnsignedByteType> w = output.randomAccess();

		int iw = (int) input.max(0);
		int ih = (int) input.max(1);
		for (int x = 1; x <= iw-1; ++x) {
			for (int y = 1; y <= ih-1; ++y) {
			double v=0;
			double m=0;
			int i=0;
			int j=0;
			double vR=0;
			double vG=0;
			double vB=0;
			double mR=0;
			double mG=0;
			double mB=0;
			RandomAccessibleInterval< UnsignedByteType > convolutionR = Views.interval( expandedViewR, new long[] { x-size/2, y-size/2 }, new long[]{ x+size/2, y+size/2} );
			RandomAccessibleInterval< UnsignedByteType > convolutionG = Views.interval( expandedViewG, new long[] { x-size/2, y-size/2 }, new long[]{ x+size/2, y+size/2} );
			RandomAccessibleInterval< UnsignedByteType > convolutionB = Views.interval( expandedViewB, new long[] { x-size/2, y-size/2 }, new long[]{ x+size/2, y+size/2} );

			final Cursor<UnsignedByteType> cR = Views.iterable(convolutionR).cursor();
			final Cursor<UnsignedByteType> cG = Views.iterable(convolutionG).cursor();
			final Cursor<UnsignedByteType> cB = Views.iterable(convolutionB).cursor();
			while(cR.hasNext() && cG.hasNext() && cB.hasNext()){
				cR.fwd();
				cG.fwd();
				cB.fwd();
				final UnsignedByteType valR = cR.get();
				final UnsignedByteType valG = cG.get();
				final UnsignedByteType valB = cB.get();
				vR=vR+valR.get()*kernel[i/size][i%size];
				vG=vG+valG.get()*kernel[i/size][i%size];
				vB=vB+valB.get()*kernel[i/size][i%size];
				i++;
				mR=mR+valR.get()*kernel2[j/size][j%size];
				mG=mG+valG.get()*kernel2[j/size][j%size];
				mB=mB+valB.get()*kernel2[j/size][j%size];
				j++;
			}
			double coeff=255/(Math.sqrt(2)*1020);
			double nR=Math.sqrt(vR*vR + mR*mR);
			nR=nR*coeff;
			double nG=Math.sqrt(vG*vG + mG*mG);
			nG=nG*coeff;
			double nB=Math.sqrt(vB*vB + mB*mB);
			nB=nB*coeff;
			w.setPosition(x, 0);
			w.setPosition(y, 1);
			w.setPosition(0, 2);
			final UnsignedByteType valR2=w.get();
			valR2.set((int) nR);

			w.setPosition(1, 2);
			final UnsignedByteType valG2=w.get();
			valG2.set((int) nG);

			w.setPosition(2, 2);
			final UnsignedByteType valB2=w.get();
			valB2.set((int) nB);
		}
	}

}



}
