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
        private static final double STRETCH_CONSTANT_2D = -0.211324865405187D;
        private static final double SQUISH_CONSTANT_2D = 0.366025403784439D;
        private static final double NORM_CONSTANT_2D = 47.0D;
        private static final long DEFAULT_SEED = 0L;
        private short[] perm;
        private short[] permGradIndex3D;
        private static byte[] gradients2D = new byte[]{5, 2, 2, 5, -5, 2, -2, 5, 5, -2, 2, -5, -5, -2, -2, -5};
        private static byte[] gradients3D = new byte[]{-11, 4, 4, -4, 11, 4, -4, 4, 11, 11, 4, 4, 4, 11, 4, 4, 4, 11, -11, -4, 4, -4, -11, 4, -4, -4, 11, 11, -4, 4, 4, -11, 4, 4, -4, 11, -11, 4, -4, -4, 11, -4, -4, 4, -11, 11, 4, -4, 4, 11, -4, 4, 4, -11, -11, -4, -4, -4, -11, -4, -4, -4, -11, 11, -4, -4, 4, -11, -4, 4, -4, -11};
        private static byte[] gradients4D = new byte[]{3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, -3, 1, 1, 1, -1, 3, 1, 1, -1, 1, 3, 1, -1, 1, 1, 3, 3, -1, 1, 1, 1, -3, 1, 1, 1, -1, 3, 1, 1, -1, 1, 3, -3, -1, 1, 1, -1, -3, 1, 1, -1, -1, 3, 1, -1, -1, 1, 3, 3, 1, -1, 1, 1, 3, -1, 1, 1, 1, -3, 1, 1, 1, -1, 3, -3, 1, -1, 1, -1, 3, -1, 1, -1, 1, -3, 1, -1, 1, -1, 3, 3, -1, -1, 1, 1, -3, -1, 1, 1, -1, -3, 1, 1, -1, -1, 3, -3, -1, -1, 1, -1, -3, -1, 1, -1, -1, -3, 1, -1, -1, -1, 3, 3, 1, 1, -1, 1, 3, 1, -1, 1, 1, 3, -1, 1, 1, 1, -3, -3, 1, 1, -1, -1, 3, 1, -1, -1, 1, 3, -1, -1, 1, 1, -3, 3, -1, 1, -1, 1, -3, 1, -1, 1, -1, 3, -1, 1, -1, 1, -3, -3, -1, 1, -1, -1, -3, 1, -1, -1, -1, 3, -1, -1, -1, 1, -3, 3, 1, -1, -1, 1, 3, -1, -1, 1, 1, -3, -1, 1, 1, -1, -3, -3, 1, -1, -1, -1, 3, -1, -1, -1, 1, -3, -1, -1, 1, -1, -3, 3, -1, -1, -1, 1, -3, -1, -1, 1, -1, -3, -1, 1, -1, -1, -3, -3, -1, -1, -1, -1, -3, -1, -1, -1, -1, -3, -1, -1, -1, -1, -3};

        public OpenSimplexNoise() {
            this(0L);
        }

        public OpenSimplexNoise(short[] perm) {
            this.perm = perm;
            this.permGradIndex3D = new short[256];

            for(int i = 0; i < 256; ++i) {
                this.permGradIndex3D[i] = (short)(perm[i] % (gradients3D.length / 3) * 3);
            }

        }

        public OpenSimplexNoise(long seed) {
            this.perm = new short[256];
            this.permGradIndex3D = new short[256];
            short[] source = new short[256];

            for(short i = 0; i < 256; source[i] = i++) {
                ;
            }

            seed = seed * 6364136223846793005L + 1442695040888963407L;
            seed = seed * 6364136223846793005L + 1442695040888963407L;
            seed = seed * 6364136223846793005L + 1442695040888963407L;

            for(int var6 = 255; var6 >= 0; --var6) {
                seed = seed * 6364136223846793005L + 1442695040888963407L;
                int r = (int)((seed + 31L) % (long)(var6 + 1));
                if(r < 0) {
                    r += var6 + 1;
                }

                this.perm[var6] = source[r];
                this.permGradIndex3D[var6] = (short)(this.perm[var6] % (gradients3D.length / 3) * 3);
                source[r] = source[var6];
            }

        }

        public double eval(double x, double y) {
            double stretchOffset = (x + y) * -0.211324865405187D;
            double xs = x + stretchOffset;
            double ys = y + stretchOffset;
            int xsb = fastFloor(xs);
            int ysb = fastFloor(ys);
            double squishOffset = (double)(xsb + ysb) * 0.366025403784439D;
            double xb = (double)xsb + squishOffset;
            double yb = (double)ysb + squishOffset;
            double xins = xs - (double)xsb;
            double yins = ys - (double)ysb;
            double inSum = xins + yins;
            double dx0 = x - xb;
            double dy0 = y - yb;
            double value = 0.0D;
            double dx1 = dx0 - 1.0D - 0.366025403784439D;
            double dy1 = dy0 - 0.0D - 0.366025403784439D;
            double attn1 = 2.0D - dx1 * dx1 - dy1 * dy1;
            if(attn1 > 0.0D) {
                attn1 *= attn1;
                value += attn1 * attn1 * this.extrapolate(xsb + 1, ysb + 0, dx1, dy1);
            }

            double dx2 = dx0 - 0.0D - 0.366025403784439D;
            double dy2 = dy0 - 1.0D - 0.366025403784439D;
            double attn2 = 2.0D - dx2 * dx2 - dy2 * dy2;
            if(attn2 > 0.0D) {
                attn2 *= attn2;
                value += attn2 * attn2 * this.extrapolate(xsb + 0, ysb + 1, dx2, dy2);
            }

            double dx_ext;
            double dy_ext;
            int xsv_ext;
            int ysv_ext;
            double attn0;
            if(inSum <= 1.0D) {
                attn0 = 1.0D - inSum;
                if(attn0 <= xins && attn0 <= yins) {
                    xsv_ext = xsb + 1;
                    ysv_ext = ysb + 1;
                    dx_ext = dx0 - 1.0D - 0.732050807568878D;
                    dy_ext = dy0 - 1.0D - 0.732050807568878D;
                } else if(xins > yins) {
                    xsv_ext = xsb + 1;
                    ysv_ext = ysb - 1;
                    dx_ext = dx0 - 1.0D;
                    dy_ext = dy0 + 1.0D;
                } else {
                    xsv_ext = xsb - 1;
                    ysv_ext = ysb + 1;
                    dx_ext = dx0 + 1.0D;
                    dy_ext = dy0 - 1.0D;
                }
            } else {
                attn0 = 2.0D - inSum;
                if(attn0 >= xins && attn0 >= yins) {
                    dx_ext = dx0;
                    dy_ext = dy0;
                    xsv_ext = xsb;
                    ysv_ext = ysb;
                } else if(xins > yins) {
                    xsv_ext = xsb + 2;
                    ysv_ext = ysb + 0;
                    dx_ext = dx0 - 2.0D - 0.732050807568878D;
                    dy_ext = dy0 + 0.0D - 0.732050807568878D;
                } else {
                    xsv_ext = xsb + 0;
                    ysv_ext = ysb + 2;
                    dx_ext = dx0 + 0.0D - 0.732050807568878D;
                    dy_ext = dy0 - 2.0D - 0.732050807568878D;
                }

                ++xsb;
                ++ysb;
                dx0 = dx0 - 1.0D - 0.732050807568878D;
                dy0 = dy0 - 1.0D - 0.732050807568878D;
            }

            attn0 = 2.0D - dx0 * dx0 - dy0 * dy0;
            if(attn0 > 0.0D) {
                attn0 *= attn0;
                value += attn0 * attn0 * this.extrapolate(xsb, ysb, dx0, dy0);
            }

            double attn_ext = 2.0D - dx_ext * dx_ext - dy_ext * dy_ext;
            if(attn_ext > 0.0D) {
                attn_ext *= attn_ext;
                value += attn_ext * attn_ext * this.extrapolate(xsv_ext, ysv_ext, dx_ext, dy_ext);
            }

            return value / 47.0D;
        }

        private double extrapolate(int xsb, int ysb, double dx, double dy) {
            int index = this.perm[this.perm[xsb & 255] + ysb & 255] & 14;
            return (double)gradients2D[index] * dx + (double)gradients2D[index + 1] * dy;
        }


        private static int fastFloor(double x) {
            int xi = (int)x;
            return x < (double)xi?xi - 1:xi;
        }
    }
