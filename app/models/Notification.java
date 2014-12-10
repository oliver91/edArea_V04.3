package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by oliver on 13.11.14.
 */
@Entity
public class Notification extends Model
{
    @Id
    public int notification_id;
    public String email_from;
    public String email_to;
    public String notification_message;
    public int type;

    public Notification(int notification_id, String email_from, String email_to, String notification_message, int type)
    {
        this.notification_id = notification_id;
        this.email_from = email_from;
        this.email_to = email_to;
        this.notification_message = notification_message;
        this.type = type; // type = 1 - adding to frinds notification // tyoe = 2 - removing from friends notification
    }

    public static Finder<Long, Notification> find = new Finder<Long, Notification>(Long.class, Notification.class);
}
