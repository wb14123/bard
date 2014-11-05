package org.apache.saltedpeanuts.handler;


import com.bardframework.bard.basic.marker.CORSHeaders;
import com.bardframework.bard.basic.marker.EscapeHTML;
import com.bardframework.bard.basic.marker.JsonParam;
import com.bardframework.bard.basic.marker.LogRequest;
import com.bardframework.bard.core.Handler;
import com.bardframework.bard.util.db.marker.DBSession;
import org.apache.saltedpeanuts.NumberString;
import org.apache.saltedpeanuts.model.Section;
import org.apache.saltedpeanuts.model.User;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import java.util.List;

@LogRequest
@CORSHeaders
@Produces("application/json")
@Path("/section")
public class SectionHandler extends Handler {

    // @LoginUser @Required("Auth error") public String userId;
    public String userId = "1";
    @DBSession public Session dbSession;
    @HeaderParam("range-start") @DefaultValue("0") @Min(0) public long rangeStart;
    @HeaderParam("range-end") @DefaultValue(NumberString.MAX_LONG_STRING) public long rangeEnd;
    @HeaderParam("range-count") @DefaultValue("20") @Max(500) public int rangeCount;

    @Path("/")
    @GET
    public List getSections(
        @QueryParam("story_id") long storyId,
        @QueryParam("current") @DefaultValue("true") boolean current
    ) {
        Query query = dbSession.getNamedQuery("section.storyAccepted");
        query.setLong("story", storyId);
        return query.list();
    }

    @Path("/")
    @PUT
    public Section create(
        @JsonParam("story_id") long storyId,
        @JsonParam("content") @EscapeHTML String content
    ) {
        Section section = new Section();
        dbSession.load(section.story, storyId);

        section.author = new User(userId);
        section.content = content;
        section.accepted = false;
        section.sectionNumber = section.story.sectionCount;

        dbSession.save(section);
        return section;
    }

    @Path("/{sectionId: \\d+}/accept")
    @GET
    public Section accept(
        @PathParam("sectionId") long sectionId
    ) {
        Section section = new Section();
        dbSession.load(section, sectionId);
        if (userId.equals(section.story.author.id)) {
            return null;
        }
        section.story.sectionCount++;
        section.accepted = true;
        dbSession.save(section.story);
        dbSession.save(section);
        return section;
    }

}
