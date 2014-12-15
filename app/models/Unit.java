package models;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by oliver on 10.12.14.
 */
@Entity
public class Unit extends Model
{
    public String email;
    @Id
    @Required
    public String unitName;
    public String courseName;
    @Required
    @Column(columnDefinition = "TEXT")
    public String unitAbout;
    public boolean current;

    public Unit(String email, String unitName, String courseName, String unitAbout, Boolean current)
    {
        this.email = email;
        this.unitName = unitName;
        this.courseName = courseName;
        this.unitAbout = unitAbout;
    }

    public static Model.Finder<String, Unit> find =
            new Model.Finder<String, Unit>(String.class, Unit.class);
}
