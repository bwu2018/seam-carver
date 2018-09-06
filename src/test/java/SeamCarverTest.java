import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverTest {

    SeamCarver seamCarver;
    SeamCarver sc6x5;
    SeamCarver sc1x1;
    SeamCarver sc12x10;
    Picture picture;
    Picture picture1;
    Picture picture2;

    @Before
    public void setup() throws Exception {
        picture = new Picture("seam-test-files/6x5.png");
        sc6x5 = new SeamCarver(picture);
        picture1 = new Picture("seam-test-files/1x1.png");
        sc1x1 = new SeamCarver(picture1);
        picture2 = new Picture("seam-test-files/12x10.png");
        sc12x10 = new SeamCarver(picture2);

    }

    @Test(timeout = 5000, expected = IllegalArgumentException.class)
    public void testSeamCarver() {
        seamCarver = new SeamCarver(null);
    }

    @Test
    public void testPicture() {
        Picture picture = new Picture("seam-test-files/6x5.png");
        assertEquals(sc6x5.picture(), picture);
    }

    @Test
    public void testPictureDefensiveCopy() {
        Picture picture = new Picture("seam-test-files/6x5.png");
        SeamCarver sc6x5 = new SeamCarver(picture);
        picture.setRGB(0, 0, 0);
        assertNotEquals(sc6x5.picture(), picture);
    }

    @Test
    public void testWidth() {
        assertEquals(sc6x5.width(), 6);
    }

    @Test
    public void testWidthSmall() {
        assertEquals(sc1x1.width(), 1);
    }

    @Test
    public void testWidthLarge() {
        assertEquals(sc12x10.width(), 12);
    }

    @Test
    public void testHeight() {
        assertEquals(sc6x5.height(), 5);
    }

    @Test
    public void testHeightSmall() {
        assertEquals(sc1x1.height(), 1);
    }

    @Test
    public void testHeightLarge() {
        assertEquals(sc6x5.height(), 5);
    }

    @Test
    public void testEnergyTopBorder() {
        for (int i = 0; i < sc6x5.width(); i++) {
            assertEquals(1000, sc6x5.energy(i, 0), 0.001);
        }
    }

    @Test
    public void testEnergyLeftBorder() {
        for (int i = 0; i < sc6x5.height(); i++) {
            assertEquals(1000, sc6x5.energy(0, i), 0.001);
        }
    }

    @Test
    public void testEnergyBottomBorder() {
        for (int i = 0; i < sc6x5.width(); i++) {
            assertEquals(1000, sc6x5.energy(i, sc6x5.height() - 1), 0.001);
        }
    }

    @Test
    public void testEnergyRightBorder() {
        for (int i = 0; i < sc6x5.height(); i++) {
            assertEquals(1000, sc6x5.energy(sc6x5.height(), i), 0.001);
        }
    }

    @Test
    public void testEnergy6x5() {
        assertEquals(237.35, sc6x5.energy(1, 1), 0.01);
    }

    @Test
    public void testEnergySmall() {
        assertEquals(1000.00, sc1x1.energy(0, 0), 0.01);
    }

    @Test
    public void testEnergyLarge() {
        assertEquals(218.02, sc12x10.energy(1, 1), 0.01);
    }

    @Test
    public void testFindVerticalSeam() {
        int[] expected = { 4, 4, 3, 2, 1 };
        int[] actual = sc6x5.findVerticalSeam();
        for (int i = 0; i < sc6x5.height(); i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testFindVerticalSeamSmall() {
        int[] expected = { 0 };
        int[] actual = sc1x1.findVerticalSeam();
        for (int i = 0; i < sc1x1.height(); i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testFindVerticalSeamLarge() {
        int[] expected = { 7, 7, 7, 6, 6, 7, 7, 7, 8, 7 };
        int[] actual = sc12x10.findVerticalSeam();
        for (int i = 0; i < sc12x10.height(); i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testFindHorizontalSeam() {
        int[] expected = { 2, 2, 1, 2, 1, 0 };
        int[] actual = sc6x5.findHorizontalSeam();
        for (int i = 0; i < sc6x5.width(); i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testFindHorizontalSeamSmall() {
        int[] expected = { 0 };
        int[] actual = sc1x1.findHorizontalSeam();
        for (int i = 0; i < sc1x1.width(); i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testFindHorizontalSeamLarge() {
        int[] expected = { 8, 8, 7, 8, 7, 6, 5, 6, 6, 5, 4, 3 };
        int[] actual = sc12x10.findHorizontalSeam();
        for (int i = 0; i < sc12x10.width(); i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testRemoveVerticalSeamCheckPixelColors() {
        Picture original = sc6x5.picture();
        int[] seam = sc6x5.findVerticalSeam();
        sc6x5.removeVerticalSeam(seam);
        assertEquals("The width should decrease by 1", 5, sc6x5.width());
        for (int i = 0; i < sc6x5.height(); i++) {
            if (seam[i] != sc6x5.width()) {
                assertEquals(original.get(seam[i] + 1, i), sc6x5.picture().get(seam[i], i));
            } else {
                assertEquals("If removed last column, last column should be previous pixel",
                        original.get(seam[i] - 1, i), sc6x5.picture().get(seam[i], i));
            }
        }
    }

    @Test(timeout = 5000, expected = IllegalArgumentException.class)
    public void testRemoveVerticalSeamCheckPixelColorsSmall() {
        Picture original = sc1x1.picture();
        int[] seam = sc1x1.findVerticalSeam();
        sc1x1.removeVerticalSeam(seam);
        assertEquals("The width should decrease by 1", 0, sc1x1.width());
        for (int i = 0; i < sc1x1.height(); i++) {
            if (seam[i] != sc1x1.width()) {
                assertEquals(original.get(seam[i] + 1, i), sc1x1.picture().get(seam[i], i));
            } else {
                assertEquals("If removed last column, last column should be previous pixel",
                        original.get(seam[i] - 1, i), sc1x1.picture().get(seam[i], i));
            }
        }
    }

    @Test
    public void testRemoveVerticalSeamCheckPixelColorsLarge() {
        Picture original = sc12x10.picture();
        int[] seam = sc12x10.findVerticalSeam();
        sc12x10.removeVerticalSeam(seam);
        assertEquals("The width should decrease by 1", 11, sc12x10.width());
        for (int i = 0; i < sc12x10.height(); i++) {
            if (seam[i] != sc12x10.width()) {
                assertEquals(original.get(seam[i] + 1, i), sc12x10.picture().get(seam[i], i));
            } else {
                assertEquals("If removed last column, last column should be previous pixel",
                        original.get(seam[i] - 1, i), sc12x10.picture().get(seam[i], i));
            }
        }

    }

    @Test
    public void testRemoveVerticalSeamCheckPixelEnergies() {
        Picture picture = new Picture("seam-test-files/6x5.png");
        SeamCarver sc6x5RemovedSeam = new SeamCarver(picture);
        int[] seam = sc6x5.findVerticalSeam();
        sc6x5RemovedSeam.removeVerticalSeam(seam);
        assertEquals("The width should decrease by 1", 5, sc6x5RemovedSeam.width());
        double[] correct = { 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 237.34, 138.69, 185.83,
                1000.0, 1000.0, 151.023, 224.49, 103.23, 1000.0, 1000.0, 178.30, 145.15, 194.50,
                1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0 };

        int count = 0;
        for (int col = 0; col < sc6x5RemovedSeam.width(); col++) {
            for (int row = 0; row < sc6x5RemovedSeam.height(); row++) {
                assertEquals(correct[count], sc6x5RemovedSeam.energy(col, row), 0.01);
                count++;
            }
        }
    }

    @Test(timeout = 5000, expected = IllegalArgumentException.class)
    public void testRemoveVerticalSeamCheckPixelEnergiesSmall() {
        Picture picture = new Picture("seam-test-files/1x1.png");
        SeamCarver sc1x1RemovedSeam = new SeamCarver(picture);
        int[] seam = sc1x1.findVerticalSeam();
        sc1x1RemovedSeam.removeVerticalSeam(seam);
        assertEquals("The width should decrease by 1", 0, sc1x1RemovedSeam.width());
        double[] correct = { 0 };

        int count = 0;
        for (int col = 0; col < sc1x1RemovedSeam.width(); col++) {
            for (int row = 0; row < sc1x1RemovedSeam.height(); row++) {
                assertEquals(correct[count], sc1x1RemovedSeam.energy(col, row), 0.01);
                count++;
            }
        }
    }

    @Test
    public void testRemoveVerticalSeamCheckPixelEnergiesLarge() {
        Picture picture = new Picture("seam-test-files/12x10.png");
        SeamCarver sc12x10RemovedSeam = new SeamCarver(picture);
        int[] seam = sc12x10.findVerticalSeam();
        sc12x10RemovedSeam.removeVerticalSeam(seam);
        assertEquals("The width should decrease by 1", 11, sc12x10RemovedSeam.width());
        double[] correct = { 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0,
                1000.0, 1000.0, 218.0252278980577, 302.48966924508346, 211.42374511865975,
                278.4367073501624, 299.8149429231305, 248.94376875109768, 294.1496217913598,
                219.97272558205938, 1000.0, 1000.0, 149.70303938130314, 251.80945176859427,
                343.9084180417804, 201.0198995124612, 218.19257549238472, 325.11997785432993,
                198.637861446402, 253.5093686631719, 1000.0, 1000.0, 244.3378808126157,
                374.68253228566715, 253.04545046295536, 251.0537790992201, 230.37360959971087,
                158.90877886385007, 194.5019280110097, 150.86086304936745, 1000.0, 1000.0,
                283.2366501708421, 267.686383665662, 261.1264061714173, 290.57012922872855,
                229.30329260610281, 212.26163101229577, 184.6835130703334, 268.54049973886623,
                1000.0, 1000.0, 154.42150109359773, 254.9568590958086, 284.80168538827155,
                384.53478386226647, 279.7177148483807, 213.6141381088808, 326.50267992774576,
                259.27977167530827, 1000.0, 1000.0, 340.49522757301605, 205.0, 256.03124809288414,
                245.21623111042223, 293.20641193534635, 188.4542384771433, 230.72927859289987,
                227.64665602639542, 1000.0, 1000.0, 219.39690061621198, 277.17683885923805,
                269.88330811667475, 265.7009597272844, 238.62313383240948, 201.511786255792,
                242.36336356801124, 182.94261395311918, 1000.0, 1000.0, 283.5912551543154,
                198.93717601293127, 334.665504646057, 256.7975856584325, 230.0, 309.9645140979851,
                267.9440240050149, 264.6847181081673, 1000.0, 1000.0, 127.7888884058391,
                326.7828024850757, 210.6252596437569, 189.90523952750752, 330.3089462911957,
                194.40936191449217, 251.2588306905849, 219.4857626362129, 1000.0, 1000.0, 1000.0,
                1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0 };

        int count = 0;
        for (int col = 0; col < sc12x10RemovedSeam.width(); col++) {
            for (int row = 0; row < sc12x10RemovedSeam.height(); row++) {
                assertEquals(correct[count], sc12x10RemovedSeam.energy(col, row), 0.01);
                count++;
            }
        }
    }

    @Test
    public void testRemoveHorizontalSeamCheckPixelColors() {
        Picture original = sc6x5.picture();
        int[] seam = sc6x5.findHorizontalSeam();
        sc6x5.removeHorizontalSeam(seam);
        assertEquals("The height should decrease by 1", 4, sc6x5.height());
        for (int i = 0; i < sc6x5.width(); i++) {
            if (seam[i] != sc6x5.height()) {
                assertEquals(original.get(i, seam[i] + 1), sc6x5.picture().get(i, seam[i]));
            } else {
                assertEquals("If removed last column, last column should be previous pixel",
                        original.get(i, seam[i] - 1), sc6x5.picture().get(i, seam[i]));
            }
        }
    }

    @Test(timeout = 5000, expected = IllegalArgumentException.class)
    public void testRemoveHorizontalSeamCheckPixelColorsSmall() {
        Picture original = sc1x1.picture();
        int[] seam = sc1x1.findHorizontalSeam();
        sc1x1.removeHorizontalSeam(seam);
        assertEquals("The height should decrease by 1", 0, sc1x1.height());
        for (int i = 0; i < sc1x1.width(); i++) {
            if (seam[i] != sc1x1.height()) {
                assertEquals(original.get(i, seam[i] + 1), sc1x1.picture().get(i, seam[i]));
            } else {
                assertEquals("If removed last column, last column should be previous pixel",
                        original.get(i, seam[i] - 1), sc1x1.picture().get(i, seam[i]));
            }
        }
    }

    @Test
    public void testRemoveHorizontalSeamCheckPixelColorsLarge() {
        Picture original = sc12x10.picture();
        int[] seam = sc12x10.findHorizontalSeam();
        sc12x10.removeHorizontalSeam(seam);
        assertEquals("The height should decrease by 1", 9, sc12x10.height());
        for (int i = 0; i < sc12x10.width(); i++) {
            if (seam[i] != sc12x10.height()) {
                assertEquals(original.get(i, seam[i] + 1), sc12x10.picture().get(i, seam[i]));
            } else {
                assertEquals("If removed last column, last column should be previous pixel",
                        original.get(i, seam[i] - 1), sc12x10.picture().get(i, seam[i]));
            }
        }
    }

    @Test
    public void testRemoveHorizontalSeamCheckEnergies() {
        Picture picture = new Picture("seam-test-files/6x5.png");
        SeamCarver sc6x5RemovedSeam = new SeamCarver(picture);
        int[] seam = sc6x5.findHorizontalSeam();
        sc6x5RemovedSeam.removeHorizontalSeam(seam);
        assertEquals("The height should decrease by 1", 4, sc6x5RemovedSeam.height());
        double[] correct = { 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0,
                161.31645917264612, 125.23977004130916, 167.81835418094172, 135.5027674994131,
                1000.0, 1000.0, 253.42454498331452, 174.0086204761132, 227.48626332154652,
                194.5019280110097, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0 };

        int count = 0;
        for (int col = 0; col < sc6x5RemovedSeam.height(); col++) {
            for (int row = 0; row < sc6x5RemovedSeam.width(); row++) {
                assertEquals(correct[count], sc6x5RemovedSeam.energy(row, col), 0.01);
                count++;
            }
        }
    }

    @Test(timeout = 5000, expected = IllegalArgumentException.class)
    public void testRemoveHorizontalSeamCheckEnergiesSmall() {
        Picture picture = new Picture("seam-test-files/1x1.png");
        SeamCarver sc1x1RemovedSeam = new SeamCarver(picture);
        int[] seam = sc1x1.findHorizontalSeam();
        sc1x1RemovedSeam.removeHorizontalSeam(seam);
        assertEquals("The height should decrease by 1", 0, sc1x1RemovedSeam.height());
        double[] correct = { 0 };

        int count = 0;
        for (int col = 0; col < sc1x1RemovedSeam.height(); col++) {
            for (int row = 0; row < sc1x1RemovedSeam.width(); row++) {
                assertEquals(correct[count], sc1x1RemovedSeam.energy(row, col), 0.01);
                count++;
            }
        }
    }

    @Test
    public void testRemoveHorizontalSeamCheckEnergiesLarge() {
        Picture picture = new Picture("seam-test-files/12x10.png");
        SeamCarver sc12x10RemovedSeam = new SeamCarver(picture);
        int[] seam = sc12x10.findHorizontalSeam();
        sc12x10RemovedSeam.removeHorizontalSeam(seam);
        assertEquals("The height should decrease by 1", 9, sc12x10RemovedSeam.height());
        double[] correct = { 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0,
                1000.0, 1000.0, 1000.0, 1000.0, 218.0252278980577, 149.70303938130314,
                244.3378808126157, 283.2366501708421, 154.42150109359773, 356.5964105259614,
                155.00322577288512, 218.33918567220132, 283.5912551543154, 127.7888884058391,
                1000.0, 1000.0, 302.48966924508346, 251.80945176859427, 374.68253228566715,
                267.686383665662, 254.9568590958086, 212.96713361455565, 173.88501948126526,
                268.37660106648644, 198.93717601293127, 326.7828024850757, 1000.0, 1000.0,
                211.42374511865975, 343.9084180417804, 253.04545046295536, 261.1264061714173,
                249.91998719590237, 211.04264971801317, 289.04497919873995, 269.88330811667475,
                334.665504646057, 187.69389974104112, 1000.0, 1000.0, 278.4367073501624,
                201.0198995124612, 251.0537790992201, 290.57012922872855, 453.31666636028285,
                209.54951682120387, 231.93749157908906, 265.7009597272844, 227.44889535893552,
                240.5368994561957, 1000.0, 1000.0, 299.8149429231305, 218.19257549238472,
                230.37360959971087, 229.30329260610281, 238.91211773369722, 301.0979906940596,
                269.13008007281536, 285.65538678624637, 242.40255774228126, 194.40936191449217,
                1000.0, 1000.0, 248.94376875109768, 267.86190471957747, 158.90877886385007,
                308.9595442772403, 325.1907132745337, 297.7146284615521, 124.52309022827855,
                315.01587261596836, 267.9440240050149, 251.2588306905849, 1000.0, 1000.0,
                96.35870484808314, 242.09708796266014, 245.10609947530887, 306.6610506732148,
                259.27977167530827, 227.64665602639542, 311.8573391793113, 168.53782958137322,
                217.36835096213983, 219.4857626362129, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0,
                1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0 };

        int count = 0;
        for (int col = 0; col < sc12x10RemovedSeam.height(); col++) {
            for (int row = 0; row < sc12x10RemovedSeam.width(); row++) {
                assertEquals(correct[count], sc12x10RemovedSeam.energy(row, col), 0.01);
                count++;
            }
        }
    }
}
