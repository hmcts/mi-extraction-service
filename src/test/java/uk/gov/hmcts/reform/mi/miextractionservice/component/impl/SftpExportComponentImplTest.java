package uk.gov.hmcts.reform.mi.miextractionservice.component.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import uk.gov.hmcts.reform.mi.miextractionservice.exception.ExportException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SftpExportComponentImplTest {

    private static final String FIELD_ENABLED = "enabled";
    private static final String CHANNEL_TYPE = "sftp";

    private static final int SFTP_PORT = 22;
    private static final String SFTP_USER = "testUser";
    private static final String SFTP_PASSWORD = "testPassword";
    private static final String SFTP_HOST = "localhost";
    private static final String SFTP_DESTINY_FOLDER = "upload/";
    private static final String FILE_NAME = "file.txt";

    @InjectMocks
    private SftpExportComponentImpl classToTest;

    @Mock
    private Session session;
    @Mock
    private JSch jsch;
    @Mock
    private ChannelSftp channelSftp;

    @BeforeEach
    public void setUp() throws JSchException {
        ReflectionTestUtils.setField(classToTest,"port", SFTP_PORT);
        ReflectionTestUtils.setField(classToTest,"remoteUser", SFTP_USER);
        ReflectionTestUtils.setField(classToTest,"remotePassword", SFTP_PASSWORD);
        ReflectionTestUtils.setField(classToTest,"destinyFolder", SFTP_DESTINY_FOLDER);
        ReflectionTestUtils.setField(classToTest,"remoteHost", SFTP_HOST);
        ReflectionTestUtils.setField(classToTest, FIELD_ENABLED, true);
    }

    @Test
    public void testCopyDisabled() throws SftpException {
        ReflectionTestUtils.setField(classToTest, FIELD_ENABLED, false);

        classToTest.copyFile(FILE_NAME);
        verifyNoInteractions(channelSftp, session);
    }

    @Test
    public void testCopyFile() throws SftpException, JSchException {
        when(jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT)).thenReturn(session);
        when(session.openChannel(CHANNEL_TYPE)).thenReturn(channelSftp);

        classToTest.copyFile(FILE_NAME);
        verify(channelSftp, times(1)).put(FILE_NAME, SFTP_DESTINY_FOLDER + FILE_NAME);
        verify(session, times(1)).disconnect();
    }

    @Test
    public void testCopyFileException() throws SftpException, JSchException {
        when(jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT)).thenReturn(session);
        when(session.openChannel(CHANNEL_TYPE)).thenReturn(channelSftp);

        doThrow(new SftpException(1, "TestError")).when(channelSftp).put(FILE_NAME, SFTP_DESTINY_FOLDER + FILE_NAME);
        assertThrows(ExportException.class, () -> classToTest.copyFile(FILE_NAME));
        verify(session, times(1)).disconnect();
    }

    @Test
    public void testCopySessionNonCreated() throws SftpException, JSchException {
        doThrow(new JSchException()).when(jsch).getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        assertThrows(ExportException.class, () -> classToTest.copyFile(FILE_NAME));
        verify(channelSftp, never()).connect(anyInt());
        verify(session, never()).disconnect();
    }

    @Test
    public void testLoadFile() throws SftpException, JSchException {
        when(jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT)).thenReturn(session);
        when(session.openChannel(CHANNEL_TYPE)).thenReturn(channelSftp);
        classToTest.loadFile(FILE_NAME, FILE_NAME);
        verify(channelSftp, times(1)).get(SFTP_DESTINY_FOLDER + FILE_NAME, FILE_NAME);
        verify(session, times(1)).disconnect();
    }

    @Test
    public void testLoadFileException() throws SftpException, JSchException {
        when(jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT)).thenReturn(session);
        when(session.openChannel(CHANNEL_TYPE)).thenReturn(channelSftp);
        doThrow(new SftpException(1, "TestError")).when(channelSftp).get(SFTP_DESTINY_FOLDER + FILE_NAME, FILE_NAME);
        assertThrows(ExportException.class, () -> classToTest.loadFile(FILE_NAME, FILE_NAME));
        verify(session, times(1)).disconnect();
    }

    @Test
    public void testLoadFileSessionNonCreated() throws SftpException, JSchException {
        doThrow(new JSchException()).when(jsch).getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        assertThrows(ExportException.class, () -> classToTest.loadFile(FILE_NAME, FILE_NAME));
        verify(channelSftp, never()).connect(anyInt());
        verify(session, never()).disconnect();
    }

    @Test
    public void testCheckConnectionDisabled() throws SftpException {
        ReflectionTestUtils.setField(classToTest, FIELD_ENABLED, false);

        classToTest.checkConnection();
        verifyNoInteractions(channelSftp, session);
    }

    @Test
    public void testCheckConnection() throws SftpException, JSchException {
        when(jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT)).thenReturn(session);
        when(session.openChannel(CHANNEL_TYPE)).thenReturn(channelSftp);

        classToTest.checkConnection();
        verify(channelSftp, times(1)).connect(anyInt());
        verify(session, times(1)).disconnect();
    }

    @Test
    public void testCheckConnectionException() throws SftpException, JSchException {
        when(jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT)).thenReturn(session);
        when(session.openChannel(CHANNEL_TYPE)).thenReturn(channelSftp);

        doThrow(new JSchException()).when(channelSftp).connect(anyInt());
        assertThrows(ExportException.class, () -> classToTest.checkConnection());
        verify(session, times(1)).disconnect();
    }

    @Test
    public void testCheckConnectionSessionNonCreated() throws SftpException, JSchException {
        doThrow(new JSchException()).when(jsch).getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        assertThrows(ExportException.class, () -> classToTest.checkConnection());
        verify(channelSftp, never()).connect(anyInt());
        verify(session, never()).disconnect();
    }
}
