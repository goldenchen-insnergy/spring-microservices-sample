package com.example;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleDirectoryResource {
    private static final String ADMIN_ACCOUNT = "rolemanager@insnergy.com"; 
    private static final String ADMIN_DOMAIN = "insnergy.com";
    private static final String APPLICATION_NAME = "GoogleDirectory";
    private static final String SERVICE_ACCOUNT_EMAIL = "your-service@m-center-20170515.iam.gserviceaccount.com";

    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.dir"), "\\src\\main\\resources\\test");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/admin-directory_v1-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static Credential authorizeServiceAccount() throws Exception {
        // Construct a GoogleCredential object with the service account email
        // and p12 file downloaded from the developer console.
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountPrivateKeyFromP12File(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\client.p12"))
                .setServiceAccountScopes(Collections.singleton(DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY))
                .setServiceAccountUser(ADMIN_ACCOUNT)
                .build();
        return credential;
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws Exception {
        // Load client secrets.
        InputStream in = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\client_secret_123.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Admin SDK Directory client service.
     * @return an authorized Directory client service
     * @throws IOException
     */
    public static Directory getDirectoryService() throws Exception {
        Credential credential = authorizeServiceAccount();//authorize();
//        Credential credential = authorize();
        return new Directory.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
//                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void getDomainUsers() throws Exception {
        // Build a new authorized API client service.
        Directory directory = getDirectoryService();

        // Print the first 10 users in the domain.
        Users result = directory.users().list()
                .setDomain(ADMIN_DOMAIN)
//                .setCustomer(ADMIN_ACCOUNT)
//                .setMaxResults(10)
                .setOrderBy("email")
                .execute();

        List<User> users = result.getUsers();
        if (users == null || users.size() == 0) {
            System.out.println("No users found.");
        } else {
            System.out.println("Users:" + users.size());
            for (User user : users) {
                System.out.println(user.getName().getFullName());
                System.out.println(user.getPrimaryEmail());
            }
        }
    }
}
