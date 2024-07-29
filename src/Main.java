import SMTP.SMTP_Connection;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class serves as the main entry of the program
 */
public class Main extends Application
{
    /**
     *This function handles the execution of the program
     * @param args is an array list of all the arguments needed to run the program
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * This function handles the creation the stage for the javafx application
     * @param stage is the stage where all the scenes will be rendered
     * @throws Exception this method can throw any exception
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        // creating the SMTP_connection object
        SMTP_Connection client = new SMTP_Connection();
        // adding the scene to the stage
        stage.setScene(client.Main_page());
        // accessing the method to change between multiple scenes
        client.change_scenes(stage);
        // execute the transmission of the email
        client.execute(stage);
        // display stage
        stage.show();
    }
}
