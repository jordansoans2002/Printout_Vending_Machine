import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

public class PrintFromDrive {

    public static final JsonFactory JSON_FACTORY=GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH="tokens";
    private static final List<String> SCOPES= Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH="/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)throws IOException {
        InputStream in = PrintFromDrive.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if(in==null)
            throw new FileNotFoundException("Credentials not found at "+CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,JSON_FACTORY,clientSecrets,SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow,receiver).authorize("user");
        return credential;
    }

    public static void main(String... args) throws GeneralSecurityException, IOException {
        Scanner sc=new Scanner(System.in);
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT,JSON_FACTORY,getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Wait kyu")
                .build();

        System.out.println("enter your email address");
        String userEmail=sc.nextLine()+"@gmail.com";

        List<File> queuedFiles = getUserFiles(service,userEmail);
        downloadFiles(service,queuedFiles);
    }

    public static List<File> getUserFiles(Drive service, String userEmail) throws IOException {
        FileList queueFile = service.files().list()
                .setQ("name = 'Queue' and mimeType='application/vnd.google-apps.folder'")
                .setFields("nextPageToken, files(id,name)")
                .execute();

        List <File> queueFiles = queueFile.getFiles();

        for(File file:queueFiles){
            System.out.println(file.getName()+" ID: "+file.getId());
            FileList filesInQueue = service.files().list()
                    .setQ("('"+userEmail+"' in owners or '"+userEmail+"' in writers or '"+userEmail+"' in readers) and '"+file.getId()+"' in parents")
                    .setFields("nextPageToken, files(id,name,sharingUser,properties)")//,owners,writers,readers)")
                    .execute();
            List<File> files = filesInQueue.getFiles();
            for(File f:files) {
                System.out.println(f.getName()+" "+f.getProperties());
            }
            return files;
        }
        return null;
    }

    public static void downloadFiles(Drive service,List<File> files) throws IOException{
        for(File file:files){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
                Map<String,String> properties = file.getProperties();
                PrintPdf.printPdf(outputStream.toByteArray(),properties);
            }catch (GoogleJsonResponseException e){
                e.printStackTrace();
            }
        }
    }
}
