import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

/* class to demonstarte use of Drive files list API */
public class DriveFunctions {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = DriveQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        //uploadBasic(service,createFolder(service));
        printFileNames(service);

        //UploadBasic.uploadBasic(CreateFolder.createFolder());
        //UploadBasic.uploadBasic("1GoLUdxb66DA3AvWAP1MMawJUx4qbTjmH");
        //DownloadFile.downloadFile("1awVnwz8E4N7JbcxAhEeDYze3B989TK8j");
        //SearchFile.searchFile();
    }

    static void printFileNames(Drive service) throws IOException {
        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                //.setQ("mimeType !='application/vnd.google-apps.folder'")
                .setQ("mimeType ='application/pdf'")
                //.setQ("sharedWithMe")
                //.setQ("name = '1665930422514.jpg'")
                //.setPageSize(1000)
                .setFields("nextPageToken, files(id, name,sharedWithMeTime,sharingUser,properties)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                //if(file.getId().equalsIgnoreCase("1GoLUdxb66DA3AvWAP1MMawJUx4qbTjmH"))
                    System.out.print(file.getName()+" "+((file.getSharingUser()==null)?"null ":file.getSharingUser().getEmailAddress()));
                    System.out.printf("%s %s %s\n", file.getId(),file.getSharedWithMeTime(),file.getProperties());
                    //downloadFiles(service, file.getId());
            }
        }
    }

    static String createFolder(Drive service) throws IOException{
        // File's metadata.
        File fileMetadata = new File();
        fileMetadata.setName("Printout Vending Machine");
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = service.files().create(fileMetadata)
                    .setFields("id,mimeType")
                    .execute();
            System.out.println("Folder ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
    }

    static void uploadBasic(Drive service,String parentId) throws IOException{
        // Upload file photo.jpg on drive.
        File fileMetadata = new File();
        fileMetadata.setName("Proposal.pdf");
        fileMetadata.setParents(Arrays.asList(parentId));
        Map<String, String> map = new HashMap<>();
        map.put("pages","10");
        fileMetadata.setProperties(map);

        // File's content.
        java.io.File filePath = new java.io.File("src/main/resources/Test files/Proposal.pdf");
        // Specify media type and file-path for file.
        FileContent mediaContent = new FileContent("application/pdf", filePath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id, name, parents,properties")
                    .execute();
            System.out.printf("%s %s %s %s\n",file.getId(),file.getName(),file.getParents(),file.getProperties());
            //return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    static void downloadFiles(Drive service,String realFileId) throws IOException{
        try{
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            service.files().get(realFileId)
                    .executeMediaAndDownloadTo(outputStream);
            System.out.println("creating pdf");
            PDDocument pdfDoc=PDDocument.load(outputStream.toByteArray());
            pdfDoc.save("src/main/resources/Outputs/" + realFileId + ".pdf");
            pdfDoc.close();
        }catch(GoogleJsonResponseException e) {
            System.err.println("Unable to move file: " + e.getDetails());
            //throw e;
        }
    }
}