package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by oliver on 12.11.14.
 */

@Entity
public class Friends extends Model
{

    @Id
    public int id;
    public String user_email;
    public String friend_email;

    public Friends(int id, String user_email, String friend_email)
    {
        this.id = id;
        this.user_email = user_email;
        this.friend_email = friend_email;
    }

    public static Finder<String, Friends> find = new Finder<String, Friends>(String.class, Friends.class);
}
