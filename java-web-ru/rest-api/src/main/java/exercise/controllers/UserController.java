package exercise.controllers;

import io.javalin.http.Context;
import io.javalin.apibuilder.CrudHandler;
import io.ebean.DB;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import exercise.domain.User;
import exercise.domain.query.QUser;

import io.javalin.validation.BodyValidator;
import io.javalin.validation.ValidationError;
import io.javalin.validation.JavalinValidation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class UserController implements CrudHandler {

    public void getAll(Context ctx) {
        // BEGIN
        List<User> users = new QUser().findList();

        ctx.json(DB.json().toJson(users));
        // END
    };

    public void getOne(Context ctx, String id) {

        // BEGIN
        User user = new QUser().id.equalTo(Integer.parseInt(id)).findOne();

        ctx.json(DB.json().toJson(user));
        // END
    };

    public void create(Context ctx) {

        // BEGIN
        User newUser = DB.json().toBean(User.class, ctx.body());

        BodyValidator<User> bodyValidator = ctx.bodyValidator(User.class)
                .check(Objects::nonNull, "Cannot be null")
                .check(user -> !user.getEmail().isEmpty(), "Cannot be empty")
                .check(user -> EmailValidator.getInstance().isValid(user.getEmail()), "Must be email")
                .check(user -> !user.getFirstName().isEmpty(), "Cannot be empty")
                .check(user -> !user.getLastName().isEmpty(), "Cannot be empty")
                .check(user -> !user.getPassword().isEmpty(), "Cannot be empty")
                .check(user -> StringUtils.isNumeric(user.getPassword()), "Password must contain only numbers");

        Map<String, List<ValidationError<? extends Object>>> errors = JavalinValidation.collectErrors(
                bodyValidator
        );

        if (!errors.isEmpty()) {
            ctx.status(422);
            ctx.attribute("errors", errors);
            ctx.attribute("user", newUser);
            return;
        }

        newUser.save();
        // END
    };

    public void update(Context ctx, String id) {
        // BEGIN
        User updateUser = DB.json().toBean(User.class, ctx.body());
        updateUser.setId(id);

        updateUser.update();
        // END
    };

    public void delete(Context ctx, String id) {
        // BEGIN
        User user = new QUser().id.equalTo(Integer.parseInt(id)).findOne();

        user.delete();
        // END
    };
}
