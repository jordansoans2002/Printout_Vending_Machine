import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import org.apache.pdfbox.pdmodel.PDDocument;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.util.List;

public class DownloadFile {
    public static void downloadFile(String realFileId) throws IOException{
        GoogleCredentials credentials=GoogleCredentials.fromStream(new FileInputStream("src/main/resources/service-account.json"));
        credentials=credentials.createScoped(Arrays.asList(DriveScopes.DRIVE));
        HttpRequestInitializer requestInitializer=new HttpCredentialsAdapter(credentials);

        Drive service=new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName("Wait kyu")
                .build();

        try{
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

            service.files().get(realFileId)
                    .executeMediaAndDownloadTo(outputStream);

//                    byte[] byteArray = outputStream.toByteArray();
//
//                    // create the object of ByteArrayInputStream class
//                    // and initialized it with the byte array.
//                    ByteArrayInputStream inStreambj = new ByteArrayInputStream(byteArray);
//
//                    // read image from byte array
//                    BufferedImage newImage = ImageIO.read(inStreambj);
//
//                    // write output image
//                    ImageIO.write(newImage, "jpg", new java.io.File("src/main/resources/Outputs/" + realFileId + ".jpg"));
//                    System.out.println("Image generated from the byte array.");

            PDDocument pdfDoc=PDDocument.load(outputStream.toByteArray());
            pdfDoc.save("src/main/resources/Outputs/" + realFileId + ".pdf");
            pdfDoc.close();
        }catch(GoogleJsonResponseException e){
            System.err.println("Unable to move file: "+e.getDetails());
            service.files().export(realFileId,"application/pdf");
            //throw e;
        }
    }
}
