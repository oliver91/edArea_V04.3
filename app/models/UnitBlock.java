package models;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by oliver on 15.12.14.
 */
@Entity
public class UnitBlock extends Model
{
    @Id
    public int id;
    @Required
    public String blockName;
    public String unitName;
    @Column(columnDefinition = "TEXT")
    public String content;
    public int blockType; // 1-text, 2-image, 3-video.

    public UnitBlock(String blockName, int id, String unitName, String content, int blockType)
    {
        this.id = id;
        this.content = content;
        this.blockName = blockName;
        this.unitName = unitName;
        this.blockType = blockType;
    }

    public static Model.Finder<String, UnitBlock> find =
            new Model.Finder<String, UnitBlock>(String.class, UnitBlock.class);
}
