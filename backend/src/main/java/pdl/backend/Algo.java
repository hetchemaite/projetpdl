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
import net.imglib2.exception.IncompatibleTypeException;

public class Algo {

    public static void light(Img<UnsignedByteType> img, int delta) {
		final RandomAccess<UnsignedByteType> r = img.randomAccess();

		final int iw = (int) img.max(0);
		final int ih = (int) img.max(1);

		for (int x = 0; x <= iw; ++x) {
			for (int y = 0; y <= ih; ++y) {
				r.setPosition(x, 0);
				r.setPosition(y, 1);
				final UnsignedByteType val = r.get();
				if(val.get() + delta > 255) {
					val.set(255);
				} else if(val.get()+delta < 0 ) {
					val.set(0);
				} else {
					val.set(val.get()+delta);
				}
			}
		}

	}
}
