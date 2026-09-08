import java.util.*;


// User Defined Exception
class InvalidDurationException extends Exception
{
    InvalidDurationException(String msg)
    {
        super(msg);
    }
}


// Shared Resource
class MusicPlayer
{
    synchronized void play(String user, String song)
    {
        System.out.println(user + " started playing " + song);


        try
        {
            Thread.sleep(1000);
        }
        catch(Exception e){}


        System.out.println(user + " finished playing " + song);
    }
}


// Thread Class
class UserThread extends Thread
{
    MusicPlayer player;
    String user;
    String song;


    UserThread(MusicPlayer player, String user, String song)
    {
        this.player = player;
        this.user = user;
        this.song = song;
    }


    public void run()
    {
        player.play(user, song);
    }
}


public class Lab4
{
    private String songTitle;
    private String artistName;
    private double duration;
    private int releaseYear;
    private boolean premium;


    public void input(String title,String artist,double dur,int year,boolean premium)
    {
        songTitle=title;
        artistName=artist;
        duration=dur;
        releaseYear=year;
        this.premium=premium;
    }


    public void display()
    {
        System.out.println("\n------ Song Details ------");
        System.out.println("Song Title : "+songTitle);
        System.out.println("Artist     : "+artistName);
        System.out.println("Duration   : "+duration+" mins");
        System.out.println("Year       : "+releaseYear);
        System.out.println("Premium    : "+premium);
    }


    public void checkEra()
    {
        if(releaseYear>=2015)
            System.out.println("Era : Streaming Era");
        else if(releaseYear>=2000)
            System.out.println("Era : Digital Era");
        else
            System.out.println("Era : Classic Era");
    }


    public static void main(String args[])
    {
        Scanner sc=new Scanner(System.in);
        Lab4 obj=new Lab4();


        try
        {
            String title;


            while(true)
            {
                System.out.print("Enter Song Title : ");
                title=sc.nextLine();


                if(title.trim().isEmpty())
                    System.out.println("Song title cannot be empty.");
                else
                    break;
            }


            String artist;


            while(true)
            {
                System.out.print("Enter Artist Name : ");
                artist=sc.nextLine();


                if(artist.trim().isEmpty())
                    System.out.println("Artist name cannot be empty.");
                else
                    break;
            }


            System.out.print("Enter Duration (mins) : ");


            double duration=sc.nextDouble();


            // User Defined Exception
            if(duration<=0)
                throw new InvalidDurationException("Duration must be greater than 0.");


            System.out.print("Enter Release Year : ");
            int year=sc.nextInt();


            System.out.print("Premium User? (true/false) : ");
            boolean premium=sc.nextBoolean();


            // Extra Complexity
            if(duration>8 && !premium)
            {
                System.out.println("Songs longer than 8 mins require Premium.");
                return;
            }


            obj.input(title,artist,duration,year,premium);


            obj.display();


            obj.checkEra();


            System.out.println("\n--- Multithreading Demo ---");


            MusicPlayer player=new MusicPlayer();


            UserThread t1=new UserThread(player,"User1",title);
            UserThread t2=new UserThread(player,"User2",title);


            // Thread Priority
            t1.setPriority(Thread.MAX_PRIORITY);
            t2.setPriority(Thread.MIN_PRIORITY);


            t1.start();
            t2.start();


            t1.join();
            t2.join();


            System.out.println("\nAll songs played successfully.");


        }


        // Built-in Exception
        catch(InputMismatchException e)
        {
            System.out.println("Invalid Input! Please enter correct data type.");
        }


        // User Defined Exception
        catch(InvalidDurationException e)
        {
            System.out.println(e.getMessage());
        }


        catch(Exception e)
        {
            System.out.println(e);
        }


        sc.close();
    }
}

