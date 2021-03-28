package pdl.backend;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
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
}
