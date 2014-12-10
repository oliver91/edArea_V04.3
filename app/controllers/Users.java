package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Query;
import models.Friends;
import models.Notification;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.allUsers;
import views.html.friendsList;
import views.html.notifications;
import views.html.userpage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by oliver on 12.11.14.
 */

@Security.Authenticated(Secured.class)
public class Users extends Controller
{

    public static void onlineUpdate(int id) {   // время последней активности

        User user = User.find.where().like("id", "%"+id+"%").findUnique();
        user.lastVisit = java.util.Calendar.getInstance().getTime();
        user.save();

    }

    public static boolean isOnline(int id) {   // проверка на онлайн
        Date currentDate = java.util.Calendar.getInstance().getTime();
        User user = User.find.where().like("id", "%"+id+"%").findUnique();


        if(user.lastVisit!=null && (currentDate.getTime()-user.lastVisit.getTime()<=300000)) {
                return true;
        }
            return false;
    }



    public static Result showUserPage(Long user) {   // генерация страницы пользователя

        // return ok(userpage.render(User.find.
        return ok(userpage.render(User.find.where().like("id", "%" + user + "%").findUnique()));

    }

    public static Result showAll()
    {
        Query<User> query = Ebean.createQuery(User.class);
        Query<Friends> query2 = Ebean.createQuery(Friends.class);
        query2.where(Expr.like("user_email", "%" + request().username() + "%"));
        List<Friends> friends = query2.findList();

        query.where(
                Expr.not(Expr.like("email", "%" + request().username() + "%")));
        List<User> all = query.findList();

        ListIterator<Friends> litr = friends.listIterator();

        while(litr.hasNext()) {
            Friends element = litr.next();
            if(all.contains(User.find.byId(element.friend_email)))
            {
                all.remove(User.find.byId(element.friend_email));
            }
        }

//        List<User> all = User.find.where().not(User.find.where().like("email", "%" + request().username() + "%"));

        if(!all.isEmpty())
            return ok(allUsers.render(all, Form.form(SelectFriend.class)));
        else
            return redirect(routes.Users.showAllFriends());
    }


    public static Result showAllFriends()
    {
        List<Friends> friends = Friends.find.where().like("user_email", "%"+request().username()+"%").findList();

        List<User> friendlyUsers = new ArrayList<>();
        ListIterator<Friends> litr = friends.listIterator();
        while(litr.hasNext())
        {
            Friends friend = litr.next();
            friendlyUsers.add(User.find.byId(friend.friend_email));
        }
        return ok(friendsList.render(friendlyUsers));
    }



    public static Result addFriend()
    {
        Form<SelectFriend> sel_form = Form.form(SelectFriend.class).bindFromRequest();
        String email = sel_form.get().email;
        int friendsId = email.hashCode()+request().username().hashCode();
        new Friends(friendsId, request().username(), email).save();

        int notification_id = email.hashCode()+request().username().hashCode();
        new Notification(notification_id, request().username(), email, "User "+ User.find.byId(request().username()).name+" inviting you to friends", 1).save();

        List<Friends> friends = Friends.find.where().like("user_email", "%"+request().username()+"%").findList();
        List<User> friendlyUsers = new ArrayList<>();
        ListIterator<Friends> litr = friends.listIterator();
        while(litr.hasNext())
        {
            Friends friend = litr.next();
            friendlyUsers.add(User.find.byId(friend.friend_email));
        }
        return ok(friendsList.render(friendlyUsers));
    }

    public static Result removeFriend()
    {
        Form<SelectFriend> sel_form = Form.form(SelectFriend.class).bindFromRequest();
        String email = sel_form.get().email;
        Friends.find.where().and(Expr.like("friend_email", "%"+email+"%"), Expr.like("user_email", "%"+request().username()+"%")).findUnique().delete();

        int notification_id = email.hashCode() + request().username().hashCode();
        new Notification(notification_id, request().username(), email, "User " + User.find.byId(request().username()).name + " removed you from friends", 2).save();

        List<Friends> friends = Friends.find.where().like("user_email", "%"+request().username()+"%").findList();
        List<User> friendlyUsers = new ArrayList<>();
        ListIterator<Friends> litr = friends.listIterator();
        while(litr.hasNext())
        {
            Friends friend = litr.next();
            friendlyUsers.add(User.find.byId(friend.friend_email));
        }
//        return redirect(routes.Users.removeFriend());
        if(friendlyUsers.isEmpty()) {
            return redirect(routes.Users.showAll());
        }else {
            return ok(friendsList.render(friendlyUsers));
        }
    }

    public static Result showNotifications()
    {
        List<Notification> notificationsList = new ArrayList<>();
        notificationsList = Notification.find.where().like("email_to", "%"+request().username()+"%").findList();
        return ok(notifications.render(notificationsList));
    }

    public static Result confirmFriend()
    {
        Form<SelectFriend> sel_form = Form.form(SelectFriend.class).bindFromRequest();
        String email = sel_form.get().email;
        int friendsId = email.hashCode()+request().username().hashCode()/ DATE.hashCode();
        new Friends(friendsId, request().username(), email).save();
        Notification.find.where().and(Expr.like("email_to", "%"+request().username()+"%"), Expr.like("email_from", "%"+email+"%")).findUnique().delete();
//        int notification_id = email.hashCode()+request().username().hashCode();
//        new Notification(notification_id, request().username(), email, "User "+User.find.byId(request().username()).name+" inviting you to friends").save();

        List<Friends> friends = Friends.find.where().like("user_email", "%"+request().username()+"%").findList();
        List<User> friendlyUsers = new ArrayList<>();
        ListIterator<Friends> litr = friends.listIterator();
        while(litr.hasNext())
        {
            Friends friend = litr.next();
            friendlyUsers.add(User.find.byId(friend.friend_email));
        }
        return ok(friendsList.render(friendlyUsers));
//        return play.mvc.Results.TODO;
    }

    public static Result confirmRemoveFriend() {
        Form<SelectFriend> sel_form = Form.form(SelectFriend.class).bindFromRequest();
        String email = sel_form.get().email;

        Friends.find.where().and(Expr.like("friend_email", "%"+email+"%"), Expr.like("user_email", "%"+request().username()+"%")).findUnique().delete();

        Notification.find.where().like("email_from", "%"+email+"%").findUnique().delete();

        List<Friends> friends = Friends.find.where().like("user_email", "%"+request().username()+"%").findList();
        List<User> friendlyUsers = new ArrayList<>();
        ListIterator<Friends> litr = friends.listIterator();
        while(litr.hasNext())
        {
            Friends friend = litr.next();
            friendlyUsers.add(User.find.byId(friend.friend_email));
        }
        if(friendlyUsers.isEmpty()) {
            return redirect(routes.Users.showAll());
        }else {
            return ok(friendsList.render(friendlyUsers));
        }
//        return play.mvc.Results.TODO;
    }


    public static class SelectFriend
    {
        public String email;
    }

}
