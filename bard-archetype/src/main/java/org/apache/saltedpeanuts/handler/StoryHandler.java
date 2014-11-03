package org.apache.saltedpeanuts.handler;

import com.bardframework.bard.basic.marker.*;
import com.bardframework.bard.core.Handler;
import com.bardframework.bard.util.db.marker.DBSession;
import org.apache.saltedpeanuts.Util;
import org.apache.saltedpeanuts.model.Story;
import org.apache.saltedpeanuts.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import java.util.List;

@LogRequest
@CORSHeaders
@Produces("application/json")
@Path("/story")
public class StoryHandler extends Handler {

    // @LoginUser @Required("Auth error") public String userId;
    public String userId = "1";
    @DBSession public Session dbSession;
    @HeaderParam("range-start") @DefaultValue("0") @Min(0) public long rangeStart;
    @HeaderParam("range-end") @DefaultValue(Util.MAX_LONG_STRING) public long rangeEnd;
    @HeaderParam("range-count") @DefaultValue("20") @Max(500) public int rangeCount;

    @Doc("Create a story, the author will be the current login user.")
    @PUT
    @Path("/")
    public Story create(
        @JsonParam("title") @Required String title,
        @JsonParam("description") String description,
        @JsonParam("content") String content
    ) {
        Story story = new Story();
        story.title = title;
        story.description = description;
        story.author = new User(userId);
        story.sectionCount = 0;
        dbSession.save(story);
        return story;
    }

    @Doc("Get all the stories.")
    @GET
    @Path("/")
    public List getAll(@Doc("The author of this story") @QueryParam("author") Long authorId) {
        Criteria c = dbSession.createCriteria(Story.class)
            .add(Restrictions.between("id", rangeStart, rangeEnd))
            .setMaxResults(rangeCount);
        if (authorId != null) {
            c.add(Restrictions.eq("author", authorId));
        }
        return c.list();
    }

    @Doc("Get story information by id")
    @GET
    @Path("/{storyId: \\d+}")
    public Story getById(
        @PathParam("storyId") @Required long id
    ) {
        Story story = new Story();
        dbSession.load(story, id);
        return story;
    }
}
