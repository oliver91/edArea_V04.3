package controllers;

import com.avaje.ebean.Expr;
import models.Course;
import models.Unit;
import models.UnitBlock;
import models.UnitBlocks.PictureBlock;
import models.UnitBlocks.TextBlock;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by oliver on 07.11.14.
 */

@Security.Authenticated(Secured.class)
public class Courses extends Controller
{
    public static Result creatingCourse()
    {
        return ok(creatingCourse.render(Form.form(Course.class)));
    }

    public static Result add() {

        Form<Course> creatingCourse_form = Form.form(Course.class).bindFromRequest();
        Course newCourse = new Course(
                                      request().username(),
                                      creatingCourse_form.get().courseName,
                                      null,
                                      creatingCourse_form.get().science,
                                      creatingCourse_form.get().aboutCourse,
                                      true);
        newCourse.save();
        return ok(upload.render(1, ""));
    }

    public static Result showCourses() {
        switch (User.find.byId(request().username()).userType) {   // в зависимости от типа пользователя будет выводится нужная информация

            case 1:
                return ok(courses.render(User.find.byId(request().username()), Course.find.where().like("email", "%" + request().username() + "%").findList()));
            case 2:
                return ok(courses.render(User.find.byId(request().username()), Course.find.where().findList()));
            case 3:
                return ok(courses.render(User.find.byId(request().username()), Course.find.where().like("email", "%" + request().username() + "%").findList()));
            default:
                return ok();
        }
    }

    public static Result showCoursePage(String courseName)
    {
        List<Unit> blockList = Unit.find.where(Expr.like("courseName", "%"+courseName+"%")).findList();
        return ok(course.render(User.find.byId(request().username()), Course.find.byId(courseName), blockList));
    }

    public static Result uploadFile() throws IOException {    // заливка каринки на сервер
        Course currentCourse = Course.find.where().and(Expr.like("email", "%"+request().username()+"%"), Expr.like("current", "%1%")).findUnique();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart filePart1 = body.getFile("filePart1");


        UUID id1 = UUID.randomUUID();  // уникальное имя лого

        File newFile1 = new File("public/logos/"+id1+".jpg");
        currentCourse.logoPath = "logos/"+id1+".jpg";
        currentCourse.current = false;
        currentCourse.save();
        File file1 = filePart1.getFile();
        InputStream isFile1 = new FileInputStream(file1);
        byte[] byteFile1 = org.apache.commons.io.IOUtils.toByteArray(isFile1);
        org.apache.commons.io.FileUtils.writeByteArrayToFile(newFile1, byteFile1);
        isFile1.close();

        return ok(courses.render(User.find.byId(request().username()), Course.find.where().like("email", "%"+request().username()+"%").findList()));
    }


    public static Result createUnit(String courseName)
    {
        Course currentCourse = Course.find.byId(courseName);
        currentCourse.current = true;
        currentCourse.save();
        return ok(creatingUnit.render(Form.form(UnitForm.class)));
    }

    public static Result addUnit()
    {
        Form<UnitForm> creatingUnit_form = Form.form(UnitForm.class).bindFromRequest();
        Course currentCourse = Course.find.where().and(Expr.like("email", "%"+request().username()+"%"), Expr.like("current", "%1%")).findUnique();
        Unit unit = new Unit(request().username(), creatingUnit_form.get().unitName, currentCourse.courseName, creatingUnit_form.get().unitAbout, false);
        unit.save();
        currentCourse.current = false;
        currentCourse.save();
        List<Unit> unitList = Unit.find.where(Expr.like("courseName", "%"+currentCourse.courseName+"%")).findList();
        return ok(course.render(User.find.byId(request().username()), currentCourse, unitList));
    }

    public static Result editUnit(String unitName)
    {
        Unit currentUnit = Unit.find.byId(unitName);
        currentUnit.current = true;
        currentUnit.save();
        List blockList = UnitBlock.find.all();
        return ok(unitEdit.render(currentUnit, blockList));
    }

    public static Result createTextBlock()
    {
        return ok(createTextBlock.render(Form.form(TextBlockForm.class)));
    }

    public static Result addTextBlock()
    {
        Form<TextBlockForm> creatingTextBlock_form = Form.form(TextBlockForm.class).bindFromRequest();
        Unit currentUnit = Unit.find.where().and(Expr.like("email", "%"+request().username()+"%"), Expr.like("current", "%1%")).findUnique();
        int id = UnitBlock.find.findRowCount()+1;
        TextBlock textBlock = new TextBlock(creatingTextBlock_form.get().name, id, currentUnit.unitName, creatingTextBlock_form.get().context);
        textBlock.save();
        UnitBlock unitBlock = new UnitBlock(creatingTextBlock_form.get().name, id, currentUnit.unitName, textBlock.context, 1);
        unitBlock.save();
//        currentUnit.current = false;
//        currentUnit.save();
        List<UnitBlock> blockList = UnitBlock.find.where(Expr.like("unitName", "%"+currentUnit.unitName+"%")).findList();
        return ok(unitEdit.render(currentUnit, blockList));
    }

    public static Result createPictureBlock() {
        return ok(upload.render(2, ""));
    }

    public static Result addPictureBlock() throws IOException{
        Unit currentUnit = Unit.find.where().and(Expr.like("email", "%"+request().username()+"%"), Expr.like("current", "%1%")).findUnique();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart filePart1 = body.getFile("filePart1");
        UUID uuid = UUID.randomUUID();  // уникальное имя лого

        File newFile1 = new File("public/logos/"+uuid+".jpg");
        int id = UnitBlock.find.findRowCount()+1;
        String logoPath = "logos/"+uuid+".jpg";
        PictureBlock pictureBlock = new PictureBlock("", id, currentUnit.unitName, logoPath);
        pictureBlock.save();
        UnitBlock unitBlock = new UnitBlock(pictureBlock.blockName, id, currentUnit.unitName, pictureBlock.logoPath, 2);
        unitBlock.save();
        File file1 = filePart1.getFile();
        InputStream isFile1 = new FileInputStream(file1);
        byte[] byteFile1 = org.apache.commons.io.IOUtils.toByteArray(isFile1);
        org.apache.commons.io.FileUtils.writeByteArrayToFile(newFile1, byteFile1);
        isFile1.close();
        List<UnitBlock> blockList = UnitBlock.find.where(Expr.like("unitName", "%"+currentUnit.unitName+"%")).findList();
        return ok(unitEdit.render(currentUnit, blockList));
    }

    public static Result unitContentComplite() {
        Unit currentUnit = Unit.find.where().and(Expr.like("email", "%"+request().username()+"%"), Expr.like("current", "%1%")).findUnique();
        currentUnit.current = false;
        currentUnit.save();
        return ok(courses.render(User.find.byId(request().username()), Course.find.where().like("email", "%" + request().username() + "%").findList()));
    }

    public static Result showUnitPage(String unitName) {
        Unit currentUnit = Unit.find.byId(unitName);
        List<UnitBlock> blockList = UnitBlock.find.where(Expr.like("unitName", "%"+currentUnit.unitName+"%")).findList();
        return ok(unit.render(currentUnit, blockList));
    }


    public static class UnitForm
    {
        public String unitName;
        public String unitAbout;
    }

    public static class TextBlockForm
    {
        public String name;
        public String context;
    }
}
