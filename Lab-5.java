import java.util.*;
class Song
{
String title;
String artist;
String album;
Song(String title, String artist, String album)
{
this.title = title;
this.artist = artist;
this.album = album;
}
void display()
{
System.out.println("Song: " + title +
" | Artist: " + artist +
" | Album: " + album);
}
}
public class Lab5
{
public static void main(String args[])
{
Scanner sc = new Scanner(System.in);
ArrayList<Song> songs = new ArrayList<>();
HashSet<String> artists = new HashSet<>();
System.out.print("Enter number of songs: ");
int n = sc.nextInt();
sc.nextLine();
for(int i = 0; i < n; i++)
{
System.out.println("\nEnter Song " + (i + 1) + " details");
String title;
while(true)
{
System.out.print("Song Title: ");
title = sc.nextLine();
if(title.trim().isEmpty())
System.out.println("Song title cannot be empty.");
else
break;
}
String artist;
while(true)
{
System.out.print("Artist: ");
artist = sc.nextLine();
if(artist.trim().isEmpty())
System.out.println("Artist name cannot be empty.");
else
break;
}
System.out.print("Album: ");
String album = sc.nextLine();
Song s = new Song(title, artist, album);
songs.add(s);
artists.add(artist);
}
System.out.println("\n----- Music Records -----");
Iterator<Song> it = songs.iterator();

while(it.hasNext())
{
it.next().display();
}
System.out.println("\n----- Unique Artists -----");
for(String artist : artists)
{
System.out.println(artist);
}
System.out.print("\nEnter artist to search: ");
String searchArtist = sc.nextLine();
boolean found = false;
System.out.println("\nSongs by " + searchArtist + ":");
for(Song s : songs)
{
if(s.artist.equalsIgnoreCase(searchArtist))
{
s.display();
found = true;
}
}
if(!found)
System.out.println("No songs found for this artist.");
sc.close();
}
}
