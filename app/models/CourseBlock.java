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
public class CourseBlock extends Model
{
    @Id
    @Required
    public String blockName;
    public String courseName;
    @Required
    @Column(columnDefinition = "TEXT")
    public String blockContent;

    public CourseBlock(String blockName, String courseName, String blockContent)
    {
        this.blockName = blockName;
        this.courseName = courseName;
        this.blockContent = blockContent;
    }

    public static Model.Finder<String, CourseBlock> find =
            new Model.Finder<String, CourseBlock>(String.class, CourseBlock.class);
}
