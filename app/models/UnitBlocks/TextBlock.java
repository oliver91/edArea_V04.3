package models.UnitBlocks;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by oliver on 15.12.14.
 */

@Entity
public class TextBlock extends Model
{
    @Id
    public int id;

    @Required
    public String blockName;
    public String unitName;
    @Column(columnDefinition = "TEXT")
    public String context;

    public TextBlock(String blockName, int id, String unitName, String context)
    {
        this.id = id;
        this.blockName = blockName;
        this.unitName = unitName;
        this.context = context;
    }

    public static Model.Finder<String, TextBlock> find =
            new Model.Finder<String, TextBlock>(String.class, TextBlock.class);
}
