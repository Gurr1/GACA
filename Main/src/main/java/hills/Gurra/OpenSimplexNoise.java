package hills.Gurra;

/* OpenSimplex Noise in Java.
 * by Kurt Spencer
 *
 * v1.1 (October 5, 2014)
 * - Added 2D and 4D implementations.
 * - Proper gradient sets for all dimensions, from a
 *   dimensionally-generalizable scheme with an actual
 *   rhyme and reason behind it.
 * - Removed default permutation array in favor of
 *   default seed.
 * - Changed seed-based constructor to be independent
 *   of any particular randomization library, so results
 *   will be the same when ported to other languages.
 */

/**
 * This file is a slighly modified version of
 */
public class OpenSimplexNoise {
	private static final double STRETCH_CONSTANT_2D = -0.211324865405187; // (1/Math.sqrt(2+1)-1)/2;
	private static final double SQUISH_CONSTANT_2D = 0.366025403784439; // (Math.sqrt(2+1)-1)/2;
	private static final double NORM_CONSTANT_2D = 41.0D;
	private static final long DEFAULT_SEED = 0L;
	private short[] perm;
	private short[] permGradIndex3D;
	private static byte[] gradients2D = new byte[] { 5, 2, 2, 5, -5, 2, -2, 5,
			5, -2, 2, -5, -5, -2, -2, -5 };
	private static byte[] gradients3D = new byte[] { -11, 4, 4, -4, 11, 4, -4,
			4, 11, 11, 4, 4, 4, 11, 4, 4, 4, 11, -11, -4, 4, -4, -11, 4, -4,
			-4, 11, 11, -4, 4, 4, -11, 4, 4, -4, 11, -11, 4, -4, -4, 11, -4,
			-4, 4, -11, 11, 4, -4, 4, 11, -4, 4, 4, -11, -11, -4, -4, -4, -11,
			-4, -4, -4, -11, 11, -4, -4, 4, -11, -4, 4, -4, -11 };

	public OpenSimplexNoise() {
		this(DEFAULT_SEED);
	}

	public OpenSimplexNoise(long seed) {
		this.perm = new short[256];
		this.permGradIndex3D = new short[256];
		setSeed(seed);

	}

	// 2D OpenSimplex Noise.
	public double eval(double x, double y) {

		// Place input coordinates onto grid.
		double stretchOffset = (x + y) * STRETCH_CONSTANT_2D;
		double xs = x + stretchOffset;
		double ys = y + stretchOffset;

		// Floor to get grid coordinates of rhombus (stretched square)
		// super-cell origin.
		int xsb = fastFloor(xs);
		int ysb = fastFloor(ys);

		// Skew out to get actual coordinates of rhombus origin. We'll need
		// these later.
		double squishOffset = (xsb + ysb) * SQUISH_CONSTANT_2D;
		double xb = xsb + squishOffset;
		double yb = ysb + squishOffset;

		// Compute grid coordinates relative to rhombus origin.
		double xins = xs - xsb;
		double yins = ys - ysb;

		// Sum those together to get a value that determines which region we're
		// in.
		double inSum = xins + yins;

		// Positions relative to origin point.
		double dx0 = x - xb;
		double dy0 = y - yb;

		// We'll be defining these inside the next block and using them
		// afterwards.
		double dx_ext, dy_ext;
		int xsv_ext, ysv_ext;

		double value = 0;

		// Contribution (1,0)
		double dx1 = dx0 - 1 - SQUISH_CONSTANT_2D;
		double dy1 = dy0 - 0 - SQUISH_CONSTANT_2D;
		double attn1 = 2 - dx1 * dx1 - dy1 * dy1;
		if (attn1 > 0) {
			attn1 *= attn1;
			value += attn1 * attn1 * extrapolate(xsb + 1, ysb + 0, dx1, dy1);
		}

		// Contribution (0,1)
		double dx2 = dx0 - 0 - SQUISH_CONSTANT_2D;
		double dy2 = dy0 - 1 - SQUISH_CONSTANT_2D;
		double attn2 = 2 - dx2 * dx2 - dy2 * dy2;
		if (attn2 > 0) {
			attn2 *= attn2;
			value += attn2 * attn2 * extrapolate(xsb + 0, ysb + 1, dx2, dy2);
		}

		if (inSum <= 1) { // We're inside the triangle (2-Simplex) at (0,0)
			double zins = 1 - inSum;
			if (zins > xins || zins > yins) { // (0,0) is one of the closest two
												// triangular vertices
				if (xins > yins) {
					xsv_ext = xsb + 1;
					ysv_ext = ysb - 1;
					dx_ext = dx0 - 1;
					dy_ext = dy0 + 1;
				} else {
					xsv_ext = xsb - 1;
					ysv_ext = ysb + 1;
					dx_ext = dx0 + 1;
					dy_ext = dy0 - 1;
				}
			} else { // (1,0) and (0,1) are the closest two vertices.
				xsv_ext = xsb + 1;
				ysv_ext = ysb + 1;
				dx_ext = dx0 - 1 - 2 * SQUISH_CONSTANT_2D;
				dy_ext = dy0 - 1 - 2 * SQUISH_CONSTANT_2D;
			}
		} else { // We're inside the triangle (2-Simplex) at (1,1)
			double zins = 2 - inSum;
			if (zins < xins || zins < yins) { // (0,0) is one of the closest two
												// triangular vertices
				if (xins > yins) {
					xsv_ext = xsb + 2;
					ysv_ext = ysb + 0;
					dx_ext = dx0 - 2 - 2 * SQUISH_CONSTANT_2D;
					dy_ext = dy0 + 0 - 2 * SQUISH_CONSTANT_2D;
				} else {
					xsv_ext = xsb + 0;
					ysv_ext = ysb + 2;
					dx_ext = dx0 + 0 - 2 * SQUISH_CONSTANT_2D;
					dy_ext = dy0 - 2 - 2 * SQUISH_CONSTANT_2D;
				}
			} else { // (1,0) and (0,1) are the closest two vertices.
				dx_ext = dx0;
				dy_ext = dy0;
				xsv_ext = xsb;
				ysv_ext = ysb;
			}
			xsb += 1;
			ysb += 1;
			dx0 = dx0 - 1 - 2 * SQUISH_CONSTANT_2D;
			dy0 = dy0 - 1 - 2 * SQUISH_CONSTANT_2D;
		}

		// Contribution (0,0) or (1,1)
		double attn0 = 2 - dx0 * dx0 - dy0 * dy0;
		if (attn0 > 0) {
			attn0 *= attn0;
			value += attn0 * attn0 * extrapolate(xsb, ysb, dx0, dy0);
		}

		// Extra Vertex
		double attn_ext = 2 - dx_ext * dx_ext - dy_ext * dy_ext;
		if (attn_ext > 0) {
			attn_ext *= attn_ext;
			value += attn_ext * attn_ext
					* extrapolate(xsv_ext, ysv_ext, dx_ext, dy_ext);
		}
		return value / NORM_CONSTANT_2D;
	}

	private double extrapolate(int xsb, int ysb, double dx, double dy) {
		int index = this.perm[this.perm[xsb & 255] + ysb & 255] & 14;
		return (double) gradients2D[index] * dx
				+ (double) gradients2D[index + 1] * dy;
	}

	private static int fastFloor(double x) {
		int xi = (int) x;
		return x < (double) xi ? xi - 1 : xi;
	}

	public void setSeed(long seed) {
		short[] source = new short[256];
		for (short i = 0; i < 256; source[i] = i++) {
			;
		}

		seed = seed * 6364136223846793005L + 1442695040888963407L;
		seed = seed * 6364136223846793005L + 1442695040888963407L;
		seed = seed * 6364136223846793005L + 1442695040888963407L;

		for (int var6 = 255; var6 >= 0; --var6) {
			seed = seed * 6364136223846793005L + 1442695040888963407L;
			int r = (int) ((seed + 31L) % (long) (var6 + 1));
			if (r < 0) {
				r += var6 + 1;
			}

			this.perm[var6] = source[r];
			this.permGradIndex3D[var6] = (short) (this.perm[var6]
					% (gradients3D.length / 3) * 3);
			source[r] = source[var6];
		}

	}
}
