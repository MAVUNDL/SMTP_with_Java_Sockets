package GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles the creating of the Graphical user interface and the backend
 */
public class SMTP_GUI
{
    protected final TextField sender;
    protected final TextField receiver;
    protected final TextField subject;
    protected final TextArea message;
    protected final Button send_button;
    protected final TextField host_name;
    protected final TextField port_number;
    protected final Button submit_button;
    protected Vector<String> information;
    public boolean all_information_ok = false;

    /**
     * This is the default constructor
     */
    public SMTP_GUI()
    {
        // receive the sender's mail address
        sender = new TextField();
        sender.setPromptText("your@mail.com");
        sender.setMinWidth(300);
        sender.setMinHeight(40);

        // receive the receiver's mail address
        receiver = new TextField();
        receiver.setPromptText("receiver@mail.com");
        receiver.setMinWidth(300);
        receiver.setMinHeight(40);

        // receive the subject of th email
        subject = new TextField();
        subject.setPromptText("Email subject");
        subject.setMinWidth(300);
        subject.setMinHeight(40);

        // receive the message to be sent to the recipient
        message = new TextArea();
        message.setPromptText("Type your message here");
        message.setFont(new Font("Arial", 20));
        message.setMaxSize(800, 800);

        // a button to initiate the transportation of the email
        send_button = new Button("Send");
        send_button.setFont(Font.font("Arial", 20));

        // creating form fields
        host_name = new TextField();
        host_name.setPromptText("Host Name");
        host_name.setMinWidth(300);
        host_name.setMinHeight(40);

        port_number = new TextField();
        port_number.setPromptText("Port Number");
        port_number.setMinWidth(300);
        port_number.setMinHeight(40);
        // creating a submit and continue button to trigger the retrieval of the date from the user and scene change
        submit_button = new Button("Submit & Continue");
        submit_button.setFont(Font.font("Arial", 20));
        // allocating memory
        information =  new Vector<>();
    }

    /**
     * This function creates scene with all the nodes for the Mail UI
     * @return return a scene with all the form fields and buttons
     */
    public  Scene create_SMTP_interface()
    {
        // labels for the interface and customizing it
        Label interface_label = new Label();
        interface_label.setFont(new Font("Arial", 40));
        interface_label.setText("Simple Mail Transfer Protocol Interface");
        interface_label.setUnderline(true);
        // adding the label onto the vbox to center it
        VBox header = new VBox(interface_label);
        header.setAlignment(Pos.TOP_CENTER);

        // Creating the form fields
        Text Sender_name =  new Text("Sender email:");
        Sender_name.setFont(new Font("Arial", 20));
        Text Receiver_name =  new Text("Receiver email:");
        Receiver_name.setFont(new Font("Arial", 20));
        Text Subject =  new Text("Subject:");
        Subject.setFont(new Font("Arial", 20));

        // creating a layout for the sender and receiver fields
        HBox mail_field = new HBox(Sender_name, this.sender);
        mail_field.setSpacing(54);
        HBox recipient = new HBox(Receiver_name, this.receiver);
        recipient.setSpacing(40);
        HBox subject = new HBox(Subject, this.subject);
        subject.setSpacing(104);
        // creating a layout node to layer the message box
        VBox message = new VBox(this.message);
        message.setAlignment(Pos.CENTER);
        // creating a layout node to layer the send button
        VBox buttons = new VBox(this.send_button);
        buttons.setAlignment(Pos.CENTER);

        // creating a layout for the interface
        VBox root_layer =  new VBox();
        root_layer.setSpacing(20);
        // adding nodes to the layout
        root_layer.getChildren().add(header);
        root_layer.getChildren().add(mail_field);
        root_layer.getChildren().add(recipient);
        root_layer.getChildren().add(subject);
        root_layer.getChildren().add(message);
        root_layer.getChildren().add(buttons);
        // returning the scene with all the nodes with their layouts
        return  new Scene(root_layer, 1000, 600);
    }

    /**
     * This function creates the scene that contains all the UI elements to be rendered on the stage for the main UI to be displayed at start
     * @return returns a Scene with all the node elements
     */
    public Scene Main_page()
    {
        // creating a Header for the UI
        Label welcome = new Label("Electronic Mail Transfer Protocol");
        welcome.setFont(new Font("Arial", 20));
        welcome.setUnderline(true);
        // encapsulating the Label with a VBox to align it on the scene
        VBox header = new VBox(welcome);
        header.setAlignment(Pos.CENTER);
        // creating a footer for the UI
        Text footer = new Text("Fill in the following fields to continue");
        footer.setFont(new Font("Arial", 15));
        // encapsulating the footer with a VBox to align it on the scene
        VBox footer_layer = new VBox(footer);
        footer_layer.setAlignment(Pos.CENTER);
        // Creating form fields for the UI
        Text hostName = new Text("Host Name:");
        hostName.setFont(new Font("Arial", 20));
        Text portNumber = new Text("Port Number:");
        portNumber.setFont(new Font("Arial", 20));
        // Wrapping the nodes with Horizontal layout
        HBox host_name_layer = new HBox(hostName, this.host_name);
        host_name_layer.setAlignment(Pos.CENTER);
        host_name_layer.setSpacing(28);
        HBox port_number_layer = new HBox(portNumber, this.port_number);
        port_number_layer.setAlignment(Pos.CENTER);
        port_number_layer.setSpacing(20);
        // wrapping the submit button with a layout node to align it
        VBox submit_buttons = new VBox(this.submit_button);
        submit_buttons.setAlignment(Pos.CENTER);

        // Creating a global layout node to layer the nodes
        VBox root_layer = new VBox();
        root_layer.setSpacing(50);
        root_layer.getChildren().add(header);
        root_layer.getChildren().add(footer_layer);
        root_layer.getChildren().add(host_name_layer);
        root_layer.getChildren().add(port_number_layer);
        root_layer.getChildren().add(submit_buttons);
        // returning the scene with the nodes
        return  new Scene(root_layer, 1000, 600);
    }

    /**
     * This function retrieves the data from the UI and validates it the information is valid
     * @param primaryStage Is main stage of the UI which renders all the scenes
     */
    protected void get_information(Stage primaryStage)
    {
        // Utilizing regular expression to validate the information entered by the user
        Pattern pattern = Pattern.compile("[a-zA-Z0-9.*%!±]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(sender.getText());
        Matcher matcher2 = pattern.matcher(receiver.getText());
        // Ensuring that the information is valid before storing it
        if(matcher.matches() && matcher2.matches())
        {
            this.information.add(sender.getText());
            this.information.add(receiver.getText());
            this.information.add(subject.getText());
            this.information.add(message.getText());
            // update boolean
            this.all_information_ok = true;
        }
        else
        {
            // Notify the user if the information is invalid to rectify the mistake of the information
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Sender or Receiver email address is invalid");
            alert.showAndWait();
            primaryStage.setScene(create_SMTP_interface());
        }
    }
}
