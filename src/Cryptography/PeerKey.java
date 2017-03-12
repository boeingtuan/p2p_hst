package Cryptography;

import java.math.BigInteger;

/**
 * @author boeingtuan
 */
public class PeerKey {
    public static class Public {
        private BigInteger N;
        private BigInteger E;
        
        public Public(String key) {
            this.N = new BigInteger(key.trim().split(" ")[0]);
            this.E = new BigInteger(key.trim().split(" ")[1]);
        }
        
        public Public(BigInteger N, BigInteger E) {
            this.N = N;
            this.E = E;
        }
        
        /**
         * @return the N
         */
        public BigInteger getN() {
            return N;
        }

        /**
         * @param N the N to set
         */
        public void setN(BigInteger N) {
            this.N = N;
        }

        /**
         * @return the E
         */
        public BigInteger getE() {
            return E;
        }

        /**
         * @param E the E to set
         */
        public void setE(BigInteger E) {
            this.E = E;
        }
        
        @Override
        public String toString() {
            try {
                return this.N.toString() + " " + this.E.toString();
            } catch (Exception e) {
                return "";
            }
        }
    }
    
    public static class Private {
        
        private BigInteger N;
        private BigInteger D;
        
        public Private(String key) {
            this.N = new BigInteger(key.trim().split(" ")[0]);
            this.D = new BigInteger(key.trim().split(" ")[1]);
        }
        
        public Private(BigInteger N, BigInteger D) {
            this.N = N;
            this.D = D;
        }
        
        /**
         * @return the N
         */
        public BigInteger getN() {
            return N;
        }

        /**
         * @param N the N to set
         */
        public void setN(BigInteger N) {
            this.N = N;
        }

        /**
         * @return the D
         */
        public BigInteger getD() {
            return D;
        }

        /**
         * @param D the D to set
         */
        public void setD(BigInteger D) {
            this.D = D;
        }
        
        @Override
        public String toString() {
            try {
                return this.N.toString() + " " + this.D.toString();
            } catch (Exception e) {
                return "";
            }
        }
    }
}
