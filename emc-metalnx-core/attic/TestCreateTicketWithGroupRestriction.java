 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */
 


package com.emc.metalnx.services.tests.tickets;

import com.emc.metalnx.core.domain.entity.DataGridTicket;
import com.emc.metalnx.core.domain.exceptions.DataGridConnectionRefusedException;
import com.emc.metalnx.core.domain.exceptions.DataGridException;
import com.emc.metalnx.core.domain.exceptions.DataGridTicketException;
import com.emc.metalnx.services.interfaces.IRODSServices;
import com.emc.metalnx.services.interfaces.TicketService;
import org.irods.jargon.core.exception.JargonException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test iRODS services.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-services-context.xml")
@WebAppConfiguration
public class TestCreateTicketWithGroupRestriction {
    private static final String PUBLIC_GROUP = "public";

    @Value("${irods.zoneName}")
    private String zone;

    @Value("${irods.admin.user}")
    private String username;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private IRODSServices irodsServices;

    private String targetPath, ticketString;
    private TestTicketUtils ticketUtils;
    private DataGridTicket dgt;

    @Before
    public void setUp() throws DataGridException, JargonException {
        String parentPath = String.format("/%s/home", zone);
        targetPath = String.format("%s/%s", parentPath, username);
        ticketUtils = new TestTicketUtils(irodsServices);

        dgt = new DataGridTicket(targetPath);
        dgt.addGroup(PUBLIC_GROUP);
    }

    @After
    public void tearDown() throws JargonException {
        ticketUtils.deleteTicket(ticketString);
    }

    @Test
    public void testCreateTicketWithHostRestriction() throws DataGridConnectionRefusedException,
            DataGridTicketException, JargonException {
        ticketString = ticketService.create(dgt);
        List<String> groups = ticketUtils.listAllGroupRestrictionsForSpecifiedTicket(ticketString);

        assertEquals(1, groups.size());
        assertTrue(groups.contains(PUBLIC_GROUP));
    }
}
