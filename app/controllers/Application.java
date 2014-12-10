package controllers;


import models.Friends;
import models.Notification;
import models.SimpleChat;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Application extends Controller
{
    @Security.Authenticated(Secured.class)
    public static Result chat(int idFrom, int idTo) {
        List<Friends> friends = Friends.find.where().like("user_email", "%"+request().username()+"%").findList();
        List<User> friendlyUsers = new ArrayList<>();
        ListIterator<Friends> litr = friends.listIterator();
        while(litr.hasNext())
        {
            Friends friend = litr.next();
            friendlyUsers.add(User.find.byId(friend.friend_email));
        }
        User sendTo = User.find.where().like("id", Integer.toString(idTo)).findUnique();
        return  ok(chat.render(User.find.byId(request().username()), friendlyUsers, sendTo.email));
    }

    @Security.Authenticated(Secured.class)
    public static Result messages() {
        List<Friends> friends = Friends.find.where().like("user_email", "%"+request().username()+"%").findList();
        List<User> friendlyUsers = new ArrayList<>();
        ListIterator<Friends> litr = friends.listIterator();
        while(litr.hasNext())
        {
            Friends friend = litr.next();
            friendlyUsers.add(User.find.byId(friend.friend_email));
        }
        return  ok(messages.render(User.find.byId(request().username()), friendlyUsers));
    }

    // get the ws.js script
    public static Result wsJs(String userEmail, String sendTo) {
        return ok(views.js.ws.render(userEmail, sendTo));
    }

    // Websocket interface
    public static WebSocket<String> wsInterface(String userEmail, String sendTo){
        return new WebSocket<String>(){
            // called when websocket handshake is done
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out){
                SimpleChat.start(userEmail, sendTo, in, out);
            }
        };
    }

    @Security.Authenticated(Secured.class)
    public static Result index()
    {
        List<Notification> notifications = new ArrayList<>();
        notifications = Notification.find.where().like("email_to", "%"+request().username()+"%").findList();
        return ok(index.render(User.find.byId(request().username()), notifications));
    }

    public static Result login()
    {
        return ok(login.render(Form.form(Login.class)));
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.Application.login()
        );
    }

    public static Result authenticate()
    {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);

            return redirect(
                    routes.Application.index()
            );
        }
    }

    public static Result registration()
    {
        return ok(registration.render(Form.form(Registration.class)));
    }

    public static Result addUser()
    {
        Form<Registration> reg_form = Form.form(Registration.class).bindFromRequest();
        new User(reg_form.get().email,
                reg_form.get().name,
                reg_form.get().userType,
                reg_form.get().password,
                reg_form.get().country,
                reg_form.get().birthDate,
                reg_form.get().city,
                User.find.where().findRowCount()+1).save();
        authenticate();
        return redirect(routes.Application.index());
    }


    public static Result showErrorPage()
    {
        return ok(errorpage.render("Page not found. Error", 404));
    }


    public static class Login
    {
        public String email;
        public String password;

        public String validate()
        {
            if (User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

    public static class Registration
    {
        public String name;
        public String email;
        public String password;
        public String birthDate;
        public String country;
        public String city;
        public int id;
        public int userType;
    }

}