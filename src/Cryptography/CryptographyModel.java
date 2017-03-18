package Cryptography;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JTextArea;

/**
 *
 * @author tuann
 */
public class CryptographyModel {
    
    public enum ModeCrypto {

        ENCRYPT(1),
        DECRYPT(2);

        public short type;

        private ModeCrypto(int type_) {
            type = (short) type_;
        }
    }

    public enum ModeBlockCipher {

        ECB(1),
        CBC(2);

        public short type;

        private ModeBlockCipher(int type) {
            type = (short) type;
        }

    }

    public enum ModePadding {

        PKCS5(1),
        ISO10126(2);

        public short type;

        private ModePadding(int type) {
            type = (short) type;
        }

    }

    public static String convertByteArray2HexString(byte[] bArray) {
        String hex = "";
        for (byte b : bArray) {
            int unsignedByte = b & 0xFF;
            String hexByte = Integer.toHexString(unsignedByte);
            hex += (hexByte.length() == 1 ? "0" + hexByte : hexByte) + " ";
        }
        return hex.toUpperCase();
    }

    public static byte[] convertHexString2ByteArray(String hexString) {
        String hexStrProcess = hexString.trim().replace(" ", "").toLowerCase();
        if (hexStrProcess.length() % 2 != 0) {
            return null;
        }
        byte[] bArray = new byte[hexStrProcess.length() / 2];
        for (int i = 0; i < hexStrProcess.length() / 2; i++) {
            int parseInt = Integer.parseInt(hexStrProcess.substring(2 * i, 2 * i + 2), 16);
            bArray[i] = (byte) parseInt;
        }
        return bArray;
    }

    private static byte[] createChecksum(String filename) throws Exception {
        MessageDigest complete;
        try (InputStream fis = new FileInputStream(filename)) {
            byte[] buffer = new byte[1024];
            complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
        }
        return complete.digest();
    }

    public static String getMD5Checksum(String filename) {
        String result = "";

        try {
            byte[] b = createChecksum(filename);

            for (int i = 0; i < b.length; i++) {
                result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return result.toUpperCase();
    }

    public static String fileContentHex(String filepath) {
        String res = "";
        try {
            FileInputStream fis = new FileInputStream(filepath);
            long size = 1000 > fis.getChannel().size() ? fis.getChannel().size() : 1000;
            byte[] block = new byte[1000];
            int i;
            if ((i = fis.read(block)) != -1) {
                block = Arrays.copyOf(block, i);
                res = convertByteArray2HexString(block) + (size < 1000 ? "" : " ...");
                return res;
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return res;
    }
    
    public static String genDESKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(new SecureRandom());
            SecretKey key = keyGen.generateKey();
            return convertByteArray2HexString(key.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
            return "";
        }
    }
    
    public static String genBlowfishKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
            keyGen.init(new SecureRandom());
            SecretKey key = keyGen.generateKey();
            return convertByteArray2HexString(key.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
            return "";
        }
    }

    public static String cryptoDESFile(ModeCrypto modeCrypto, ModeBlockCipher modeBlockCipher, ModePadding modePadding,
            String keyHexString, String ivHexString, String filepathSource, JTextArea _Logger) {

        try {
            SecretKey myDesKey = new SecretKeySpec(convertHexString2ByteArray(keyHexString), "DES");
            Cipher desCipher;
            String instanceString = "DES";
            AlgorithmParameterSpec IVspec = null;
            switch (modeBlockCipher) {
                case CBC:
                    instanceString += "/CBC";
                    IVspec = new IvParameterSpec(convertHexString2ByteArray(ivHexString));
                    break;
                case ECB:
                    instanceString += "/ECB";
                    break;
            }

            switch (modePadding) {
                case PKCS5:
                    instanceString += "/PKCS5Padding";
                    break;
                case ISO10126:
                    instanceString += "/ISO10126Padding";
                    break;
            }

            desCipher = Cipher.getInstance(instanceString);
            String filepathDes = filepathSource;
            FileInputStream fis = new FileInputStream(filepathSource);
            FileOutputStream fos;
            long size = fis.getChannel().size();
            long cnt = 0;
            switch (modeCrypto) {
                case ENCRYPT:
                    if (modeBlockCipher == ModeBlockCipher.ECB) {
                        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                    } else {
                        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, IVspec);
                    }
                    filepathDes += ".encrypted";
                    fos = new FileOutputStream(filepathDes);

                    try (CipherOutputStream cos = new CipherOutputStream(fos, desCipher)) {
                        byte[] block = new byte[8];
                        int i;
                        _Logger.append("Percentage completed: 0% ");
                        while ((i = fis.read(block)) != -1) {
                            cos.write(block, 0, i);
                            cnt += 8;
                            if (cnt % 1000000 == 0) {
                                _Logger.append(String.format("%.2f%% ", cnt * 100.0 / size));
                            }
                        }
                        _Logger.append("100%\n\n");
                    }
                    break;
                case DECRYPT:
                    if (modeBlockCipher == ModeBlockCipher.ECB) {
                        desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                    } else {
                        desCipher.init(Cipher.DECRYPT_MODE, myDesKey, IVspec);
                    }
                    filepathDes += ".decrypted";
                    fos = new FileOutputStream(filepathDes);

                    try (CipherInputStream cis = new CipherInputStream(fis, desCipher)) {
                        byte[] block = new byte[8];
                        int i;
                        _Logger.append("Percentage completed: 0% ");
                        while ((i = cis.read(block)) != -1) {
                            fos.write(block, 0, i);
                            cnt += 8;
                            if (cnt % 1000000 == 0) {
                                _Logger.append(String.format("%.2f %% ", cnt * 100.0 / size));
                            }
                        }
                        _Logger.append("100%\n\n");
                    }
                    break;
            }

            return filepathDes;

        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidAlgorithmParameterException ex) {
            System.out.println(ex);
            return "";
        }
    }
    public static String cryptoBlowfishFile(ModeCrypto modeCrypto, ModeBlockCipher modeBlockCipher, ModePadding modePadding,
            String keyHexString, String ivHexString, String filepathSource, JTextArea _Logger) {

        try {
            SecretKey myDesKey = new SecretKeySpec(convertHexString2ByteArray(keyHexString), "Blowfish");
            Cipher blowfishCipher;
            String instanceString = "Blowfish";
            AlgorithmParameterSpec IVspec = null;
            switch (modeBlockCipher) {
                case CBC:
                    instanceString += "/CBC";
                    IVspec = new IvParameterSpec(convertHexString2ByteArray(ivHexString));
                    break;
                case ECB:
                    instanceString += "/ECB";
                    break;
            }

            switch (modePadding) {
                case PKCS5:
                    instanceString += "/PKCS5Padding";
                    break;
                case ISO10126:
                    instanceString += "/ISO10126Padding";
                    break;
            }

            blowfishCipher = Cipher.getInstance(instanceString);
            String filepathDes = filepathSource;
            FileInputStream fis = new FileInputStream(filepathSource);
            FileOutputStream fos;
            long size = fis.getChannel().size();
            long cnt = 0;
            switch (modeCrypto) {
                case ENCRYPT:
                    if (modeBlockCipher == ModeBlockCipher.ECB) {
                        blowfishCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                    } else {
                        blowfishCipher.init(Cipher.ENCRYPT_MODE, myDesKey, IVspec);
                    }
                    filepathDes += ".encrypted";
                    fos = new FileOutputStream(filepathDes);

                    try (CipherOutputStream cos = new CipherOutputStream(fos, blowfishCipher)) {
                        byte[] block = new byte[8];
                        int i;
                        _Logger.append("Percentage completed: 0% ");
                        while ((i = fis.read(block)) != -1) {
                            cos.write(block, 0, i);
                            cnt += 8;
                            if (cnt % 1000000 == 0) {
                                _Logger.append(String.format("%.2f%% ", cnt * 100.0 / size));
                            }
                        }
                        _Logger.append("100%\n\n");
                    }
                    break;
                case DECRYPT:
                    if (modeBlockCipher == ModeBlockCipher.ECB) {
                        blowfishCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                    } else {
                        blowfishCipher.init(Cipher.DECRYPT_MODE, myDesKey, IVspec);
                    }
                    filepathDes += ".decrypted";
                    fos = new FileOutputStream(filepathDes);

                    try (CipherInputStream cis = new CipherInputStream(fis, blowfishCipher)) {
                        byte[] block = new byte[8];
                        int i;
                        _Logger.append("Percentage completed: 0% ");
                        while ((i = cis.read(block)) != -1) {
                            fos.write(block, 0, i);
                            cnt += 8;
                            if (cnt % 1000000 == 0) {
                                _Logger.append(String.format("%.2f %% ", cnt * 100.0 / size));
                            }
                        }
                        _Logger.append("100%\n\n");
                    }
                    break;
            }

            return filepathDes;

        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidAlgorithmParameterException ex) {
            System.out.println(ex);
            return "";
        }
    }

    public static BigInteger genPrime(int bit) {
        SecureRandom random = new SecureRandom();
        return BigInteger.probablePrime(bit, random);
    }

    public static BigInteger computeModulus(BigInteger p, BigInteger q) {
        BigInteger modulus = p.multiply(q);
        return modulus;
    }

    public static BigInteger computePhiModulus(BigInteger p, BigInteger q) {
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        return m;
    }

    public static BigInteger genPublicExponent(BigInteger max) {
        SecureRandom random = new SecureRandom();
        int length = max.bitLength() - 1;
        BigInteger e = BigInteger.probablePrime(length, random);
        while (!(max.gcd(e)).equals(BigInteger.ONE)) {
            e = BigInteger.probablePrime(length, random);
        }
        return e;
    }

    public static BigInteger genPrivateExponent(BigInteger publicExponent, BigInteger m) {
        BigInteger privateExponent = publicExponent.modInverse(m);
        return privateExponent;
    }

    public static String cryptoRSAFile(ModeCrypto modeCrypto, ModeBlockCipher modeBlockCipher, ModePadding modePadding,
            BigInteger modulus, BigInteger publicExponent, BigInteger privateExponent, String filepathSource, JTextArea _Logger) {

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");

            Cipher rsaCipher;
            String instanceString = "RSA";
            AlgorithmParameterSpec IVspec = null;
            switch (modeBlockCipher) {
                case CBC:
                    break;
                case ECB:
                    instanceString += "/ECB";
                    break;
            }

            switch (modePadding) {
                case PKCS5:
                    instanceString += "/PKCS1Padding";
                    break;
                case ISO10126:
                    break;
            }

            rsaCipher = Cipher.getInstance(instanceString);
            String filepathDes = filepathSource;
            FileInputStream fis = new FileInputStream(filepathSource);
            FileOutputStream fos;
            long size = fis.getChannel().size();
            long cnt = 0;

            switch (modeCrypto) {
                case ENCRYPT:
                    RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulus, publicExponent);
                    PublicKey pubKey = kf.generatePublic(publicSpec);
                    rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
                    filepathDes += ".encrypted";
                    fos = new FileOutputStream(filepathDes);
                    int key_buffer_encrypt = publicSpec.getModulus().bitLength() / 8 - 11;
                            
                    try (CipherOutputStream cos = new CipherOutputStream(fos, rsaCipher)) {
                        byte[] block = new byte[key_buffer_encrypt];
                        int i;
                        _Logger.append("Percentage completed: 0% ");
                        while ((i = fis.read(block)) != -1) {
                            //cos.write(block, 0, i);
                            byte[] cipherBlock = rsaCipher.doFinal(block, 0, i);
                            fos.write(cipherBlock);
                            cnt += key_buffer_encrypt;
                            if (cnt % 100 == 0) {
                                _Logger.append(String.format("%.2f%% ", cnt * 100.0 / size));
                            }
                        }
                        _Logger.append("100%\n");
                    }
                    finally {
                    	fos.close();
					}
                    break;
                case DECRYPT:
                    RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(modulus, privateExponent);
                    PrivateKey privKey = kf.generatePrivate(privateSpec);
                    rsaCipher.init(Cipher.DECRYPT_MODE, privKey);
                    filepathDes += ".decrypted";
                    fos = new FileOutputStream(filepathDes);
                    int key_buffer_decrypt = privateSpec.getModulus().bitLength() / 8;
                    key_buffer_decrypt = key_buffer_decrypt % 2 == 0 ? key_buffer_decrypt : key_buffer_decrypt + 1;
                    
                    try (CipherInputStream cis = new CipherInputStream(fis, rsaCipher)) {
                        byte[] block = new byte[key_buffer_decrypt];
                        int i;
                        _Logger.append("Percentage completed: 0% ");
                        while ((i = fis.read(block)) != -1) {
                            byte[] cipherBlock = rsaCipher.doFinal(block);
                            fos.write(cipherBlock);
                            cnt += key_buffer_decrypt;
                            if (cnt % 100 == 0) {
                                _Logger.append(String.format("%.2f%% ", cnt * 100.0 / size));
                            }
                        }
                        _Logger.append("100%\n");
                    }
                    finally {
                    	fos.close();
					}
                    break;
            }
            fis.close();
            return filepathDes;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException | 
                InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | 
                BadPaddingException ex) {
            System.out.println(ex);
            return "";
        }
    }

    public static void main(String[] argv) {
//        String key = "29 AD 92 7C 02 BF C2 68";
//        String iv = "29 AD 92 7C 02 BF C2 68";
//        String filepath = "/home/boeingtuan/Documents/avatar.jpg";
//        System.out.println(getMD5Checksum(filepath));
//
//        String outputEncrypt = cryptoDESFile(ModeCrypto.ENCRYPT, ModeBlockCipher.CBC, ModePadding.ISO10126, key, iv, filepath);
//        System.out.println(outputEncrypt);
//        System.out.println(getMD5Checksum(outputEncrypt));
//
//        String outputDecrypt = cryptoDESFile(ModeCrypto.DECRYPT, ModeBlockCipher.CBC, ModePadding.ISO10126, key, iv, outputEncrypt);
//        System.out.println(outputDecrypt);
//        System.out.println(getMD5Checksum(outputDecrypt));

//        String filepath = "/home/boeingtuan/Documents/UsesCaseDescriptions.xlsx";
//        System.out.println(getMD5Checksum(filepath));
//
//        BigInteger p = genPrime(1024);
//        BigInteger q = genPrime(1024);
//        BigInteger modulus = computeModulus(p, q);
//        BigInteger phiModulus = computePhiModulus(p, q);
//        BigInteger e = genPublicExponent(phiModulus);
//        BigInteger d = genPrivateExponent(e, phiModulus);
//        
//        String outputEncrypt = cryptoRSAFile(ModeCrypto.ENCRYPT, ModeBlockCipher.ECB, ModePadding.PKCS5, modulus, e, d, filepath);
//        System.out.println(outputEncrypt);
//        System.out.println(getMD5Checksum(outputEncrypt));
//
//        String outputDecrypt = cryptoRSAFile(ModeCrypto.DECRYPT, ModeBlockCipher.ECB, ModePadding.PKCS5, modulus, e, d, outputEncrypt);
//        System.out.println(outputDecrypt);
//        System.out.println(getMD5Checksum(outputDecrypt));
    }
}
