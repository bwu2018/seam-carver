import edu.princeton.cs.algs4.Picture;

/**
 * Seam-carving is a content-aware image resizing technique where the image is reduced in size by
 * one pixel of height (or width) at a time. A vertical seam in an image is a path of pixels
 * connected from the top to the bottom with one pixel in each row.
 *
 * @author bwu2018
 *
 */
public class SeamCarver {

    /**
     * Border energy.
     */
    private static final int BORDER_ENERGY = 1000;

    /**
     * Color constant.
     */
    private static final int COLOR_CONSTANT = 0xFF;

    /**
     * Given picture.
     */
    private Picture picture;

    /**
     * 2d array that stores energies.
     */
    private double[][] energy;

    /**
     * 2d array that stores pixel distances from the top.
     */
    private double[][] energyTo;

    /**
     * Create seam carver object based on the given picture.
     *
     * @param picture
     *            given picture
     */
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);

        energy = new double[picture.width()][picture.height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                energy[col][row] = energy(col, row);
            }
        }
    }

    /**
     * Returns current picture.
     *
     * @return current picture
     */
    public Picture picture() {
        return new Picture(picture);
    }

    /**
     * Returns width of current picture.
     *
     * @return width of current picture
     */
    public int width() {
        return picture.width();
    }

    /**
     * Returns height of current picture.
     *
     * @return height of current picture
     */
    public int height() {
        return picture.height();
    }

    /**
     * Calculates energy of a pixel at column x and row y.
     *
     * @param x
     *            a column value
     * @param y
     *            a row value
     * @return energy of a pixel
     */
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1)
            throw new IllegalArgumentException();
        if (x == width() - 1 || y == height() - 1 || x == 0 || y == 0) {
            return BORDER_ENERGY;
        }
        if (energy[x][y] != 0) {
            return energy[x][y];
        }
        return Math.sqrt(xgradient(x, y) + ygradient(x, y));
    }

    /**
     * Returns red value.
     *
     * @param rgb
     *            rgb value
     * @return red value
     */
    private int red(int rgb) {
        return (rgb >> 16) & COLOR_CONSTANT;
    }

    /**
     * Returns green value.
     *
     * @param rgb
     *            rgb value
     * @return green value
     */
    private int green(int rgb) {
        return (rgb >> 8) & COLOR_CONSTANT;
    }

    /**
     * Returns blue value.
     *
     * @param rgb
     *            RGB value
     * @return blue value
     */
    private int blue(int rgb) {
        return (rgb >> 0) & COLOR_CONSTANT;
    }

    /**
     * Calculates gradient with respect to x.
     *
     * @param x
     *            a column value
     * @param y
     *            a row value
     * @return gradient
     */
    private double xgradient(int x, int y) {
        int left = picture.getRGB(x - 1, y);
        int right = picture.getRGB(x + 1, y);
        int red = red(right) - red(left);
        int green = green(right) - green(left);
        int blue = blue(right) - blue(left);
        return (red * red) + (green * green) + (blue * blue);
    }

    /**
     * Calculates gradient with respect to y.
     *
     * @param x
     *            a column value
     * @param y
     *            a row value
     * @return gradient
     */
    private double ygradient(int x, int y) {
        int up = picture.getRGB(x, y - 1);
        int down = picture.getRGB(x, y + 1);
        int red = red(up) - red(down);
        int green = green(up) - green(down);
        int blue = blue(up) - blue(down);
        return (red * red) + (green * green) + (blue * blue);
    }

    /**
     * Transposes picture from horizontal to vertical or visa versa.
     */
    private void transpose() {
        Picture transposedPic = new Picture(picture.height(), picture.width());
        double[][] transposedEnergy = new double[picture.height()][picture.width()];
        for (int col = 0; col < picture.width(); col++)
            for (int row = 0; row < picture.height(); row++) {
                transposedPic.set(row, col, picture.get(col, row));
                transposedEnergy[row][col] = energy[col][row];
            }
        energy = transposedEnergy;
        picture = transposedPic;
    }

    /**
     * Finds horizontal seam.
     *
     * @return sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    /**
     * Returns sequence of indices for vertical seam.
     *
     * @return sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        energyTo = new double[width()][height()];
        int[] seam = new int[height()];

        if (width() == 1) {
            return seam;
        }

        // set top to 0 and everything else to infinity
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if (row == 0) {
                    energyTo[col][row] = 0;
                } else {
                    energyTo[col][row] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // relax everything
        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                // relax bottom left
                if (col - 1 >= 0) {
                    relax(col, row, col - 1, row + 1);
                }
                // relax bottom
                relax(col, row, col, row + 1);
                // relax bottom right
                if (col + 1 < width()) {
                    relax(col, row, col + 1, row + 1);
                }
            }
        }

        // find min value of last line and record x value
        double minEnergy = Double.POSITIVE_INFINITY;
        int x = -1;
        for (int col = 0; col < width(); col++) {
            if (energyTo[col][height() - 1] < minEnergy) {
                minEnergy = energyTo[col][height() - 1];
                x = col;
            }
        }

        // go back through and find seam

        seam[height() - 1] = x;

        for (int row = height() - 1; row > 0; row--) {
            // check top right, top, top left and see which is smallest of them
            double minValue = energyTo[x][row - 1];
            int originalX = x;
            if (x > 0) {
                if (energyTo[originalX - 1][row - 1] < minValue) {
                    minValue = energyTo[originalX - 1][row - 1];
                    x = originalX - 1;
                }
            }

            if (x < width()) {
                if (energyTo[originalX + 1][row - 1] < minValue) {
                    minValue = energyTo[originalX + 1][row - 1];
                    x = originalX + 1;
                }
            }
            seam[row - 1] = x;
        }
        return seam;
    }

    /**
     * Relax from two given points.
     *
     * @param x1
     *            x coordinate
     * @param y1
     *            y coordinate
     * @param x2
     *            x coordinate
     * @param y2
     *            y coordinate
     */
    // x1 is from, x2 is to
    private void relax(int x1, int y1, int x2, int y2) {
        if (energyTo[x2][y2] > energyTo[x1][y1] + energy[x2][y2]) {
            energyTo[x2][y2] = energyTo[x1][y1] + energy[x2][y2];
        }
    }

    /**
     * Remove horizontal seam from current picture.
     *
     * @param seam
     *            int array of seam to remove
     */
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    /**
     * Remove vertical seam from current picture.
     *
     * @param seam
     *            int array of seam to remove
     */
    public void removeVerticalSeam(int[] seam) {
        // validate seam
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (width() <= 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }

        Picture newPicture = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            int count = 0;
            for (int col = 0; col < width(); col++) {
                if (col != seam[row]) {
                    newPicture.set(count, row, picture.get(col, row));
                    count++;
                }
            }
        }
        this.picture = newPicture;

        energy = new double[picture.width()][picture.height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                energy[col][row] = energy(col, row);
            }
        }

    }
}
