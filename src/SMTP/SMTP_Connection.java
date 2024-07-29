package SMTP;

import GUI.SMTP_GUI;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Vector;

/**
 * This class handles the connection to the server and communication with the server
 */
public class SMTP_Connection extends SMTP_GUI
{
    // this vector will store the retrieved values from the GUI
    private final Vector<String> main_information;
    // creating a socket instance
    private Socket client_socket = null;

    /**
     * This is the default constructor
     */
    public SMTP_Connection()
    {
        super();
        main_information = new Vector<>();
    }

    /**
     * This method handles facilitates the communication between the server and the client and transmission of the email
     */
    public void Execute_SMTP()
    {
        // creating all the streams with automatic resource management
       try(
           BufferedReader reader = new BufferedReader(new InputStreamReader(this.client_socket.getInputStream()));
           BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.client_socket.getOutputStream())))
       {
           System.out.println("Server response: " + reader.readLine());
           // initiating connection
           writer.write("HELO " + this.main_information.getFirst() + "\r\n");
           writer.flush();
           System.out.println("Server response: " + reader.readLine());
           // sender details
           writer.write("MAIL FROM:<" + this.information.getFirst() + ">\r\n");
           writer.flush();
           System.out.println("Server response: " + reader.readLine());
           // recipient details
           writer.write("RCPT TO:<" + this.information.get(1) + ">\r\n");
           writer.flush();
           System.out.println("Server response: " + reader.readLine());
           // message block
           writer.write("DATA\r\n");
           writer.flush();
           System.out.println("Server response: " + reader.readLine());
           // message information
           writer.write("Subject: " + this.information.get(2) + "\r\n");
           writer.write("From: " + this.information.getFirst() + "\r\n");
           writer.write("To: " + this.information.get(1)+ "\r\n");
           writer.write("\r\r");
           writer.write(this.information.get(3) + "\r\n");
           writer.write(".\r\n");
           writer.flush();
           System.out.println("Server response: " + reader.readLine());
           // close connection with the server
           writer.write("QUIT\r\n");
           writer.flush();
           System.out.println("Server response: " + reader.readLine());
       }
       catch (IOException exception) // catch possible I/O exceptions
       {
           System.out.println(exception.getMessage());
       }
       catch (ArrayIndexOutOfBoundsException exception) // catching possible exception regarding accessing non-existing memory
       {
           System.out.println("ArrayIndexOutOfBoundsException: " + "cause by closing the stage without retrieving data from the field to initialize the vector");
       }
       finally { // ensuring the socket is closed
           if(client_socket != null)
           {
               try
               {
                   // closing the socket
                   client_socket.close();
               }
               catch (IOException exception) // catching exception caused by the socket
               {
                   System.out.println(exception.getMessage());
               }
           }
       }
    }

    /**
     * This function defined an event handler for the submit button
     * @param primaryStage is the main stage where all scenes are to be rendered
     */
    public void change_scenes(Stage primaryStage)
    {
        this.submit_button.setOnAction(actionEvent -> {
            retrieve_info(primaryStage);
        });
    }

    /**
     * This function will retrieve info from the UI and handle the error handling for the host and port number
     */
    public void retrieve_info(Stage primaryStage)
    {
        try
        {
            // checking if the field have data in them to ensure we don't retrieve empty strings
            if(!Objects.equals(this.host_name.getText(), "") && !Objects.equals(this.port_number.getText(), ""))
            {
                // create a socket instance with the retrieved data
                this.client_socket = new Socket(this.host_name.getText(), Integer.parseInt(this.port_number.getText()));
                // store the data
                this.main_information.add(this.host_name.getText());
                this.main_information.add(this.port_number.getText());
                // change to the next scene
                primaryStage.setScene(create_SMTP_interface());
            }
            else // if the text fields are empty
            {
                // notify the user to input the information before submitting
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("You have not specified a valid host or port number");
                alert.showAndWait();
                // reshow the stage for the user to put the correct information
                primaryStage.show();
            }
        }
        catch (UnknownHostException exception) // in case the host is unknown or DNE, handle that exception
        {
            // Create an error alert box to notify the user that the host DNE
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("This host: " + this.host_name.getText() + " does not exist. Put the correct host or try another one.");
            alert.showAndWait();
            // reshow the stage to the user puts the correct the information before continuing
            primaryStage.show();
        }
        catch (IOException exception)
        {
            System.out.println(exception.getMessage());
        }
        catch (NumberFormatException exception) // in case the port number couldn't be converted from string from to integer handle the exception
        {
            // create an Error alert box to notify the user that the port number is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Port number must be an integer. Fill on a valid port number.");
            alert.showAndWait();
            // reshow the stage to the user can rectify the mistake
            primaryStage.show();
        }
    }

    /**
     * This function retrieves information from the UI and conducts all test validations for the information retrieved before storing
     * @param primaryStage is the Stage where all the scenes will be rendered
     */
    public void execute(Stage primaryStage)
    {
        // creating an event handler for the send button
        this.send_button.setOnAction(actionEvent -> {
            // retrieve the date from the fields
            get_information(primaryStage);
            // checking if the information is valid
            if(this.all_information_ok)
            {
                // if valid then send the email
                Execute_SMTP();
                // create an alert box to show successful SMTP
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Email has been sent successfully.");
                alert.showAndWait();
                // close the stage at last
                primaryStage.close();
            }
            else
            {
                System.out.println("Cannot execute command to generate email due to an error with invalid email address");
            }
        });
    }
}
