import java.util.*;
import media.*;      // importing all classes from the media package

public class Lab3
{
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);

        String title, artist;
        double duration;

        while(true)                                         // Title Validation
        {
            System.out.print("Enter Song Title: ");
            title = sc.nextLine();

            if(!title.trim().isEmpty())
                break;

            System.out.println("Title cannot be empty.");
        }

        while(true)                                               // Artist Validation
        {
            System.out.print("Enter Artist Name: ");
            artist = sc.nextLine();

            if(!artist.trim().isEmpty())
                break;

            System.out.println("Artist name cannot be empty.");
        }

        while(true)                                                     // Duration Validation
        {
            System.out.print("Enter Duration: ");
            duration = sc.nextDouble();

            if(duration > 0 && duration <= 10)
                break;

            System.out.println("Duration should be between 1 and 10 minutes.");
        }

        Media obj = new Song(title, artist, duration);
        System.out.println("\n===== SONG DETAILS =====");
        obj.display();

        Playable p = (Song) obj;
        p.play();
        p.stop();

        Song s = (Song) obj;
        s.playSong();
        s.playSong(3);
    }
}
