package models.UnitBlocks;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by oliver on 16.12.14.
 */
@Entity
public class PictureBlock extends Model
{
    @Id
    public int id;
    @Required
    public String blockName;
    public String unitName;
    public String logoPath;

    public PictureBlock(String blockName, int id, String unitName, String logoPath)
    {
        this.blockName = blockName;
        this.id = id;
        this.unitName = unitName;
        this.logoPath = logoPath;
    }

    public static Model.Finder<String, PictureBlock> find =
            new Model.Finder<String, PictureBlock>(String.class, PictureBlock.class);
}
