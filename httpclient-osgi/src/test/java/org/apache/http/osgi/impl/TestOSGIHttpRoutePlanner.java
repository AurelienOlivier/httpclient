package org.apache.http.osgi.impl;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.osgi.services.ProxyConfiguration;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author aurelienolivier
 */
public final class TestOSGIHttpRoutePlanner {

    @Test
    public void proxyExceptions() throws HttpException {
        final OSGiHttpRoutePlanner osgiHttpRoutePlanner = new OSGiHttpRoutePlanner(null, null);
        final HttpHost httpHost = new HttpHost("www.google.fr");

        final ProxyConfiguration proxyConfigurationMock = mock(ProxyConfiguration.class);

        when(proxyConfigurationMock.isEnabled()).thenReturn(true);
        when(proxyConfigurationMock.getHostname()).thenReturn("myproxyhostname");
        when(proxyConfigurationMock.getPort()).thenReturn(8080);

        when(proxyConfigurationMock.getProxyExceptions()).thenReturn(new String[0]);
        assertNotNull(osgiHttpRoutePlanner.determineProxyFromProxyConfiguration(httpHost, proxyConfigurationMock));

        when(proxyConfigurationMock.getProxyExceptions()).thenReturn(new String[]{".google.fr"});
        assertNull(osgiHttpRoutePlanner.determineProxyFromProxyConfiguration(httpHost, proxyConfigurationMock));

        when(proxyConfigurationMock.getProxyExceptions()).thenReturn(new String[]{".google.com", ".google.fr"});
        assertNull(osgiHttpRoutePlanner.determineProxyFromProxyConfiguration(httpHost, proxyConfigurationMock));

        when(proxyConfigurationMock.getProxyExceptions()).thenReturn(new String[]{".google.fr", ".google.com"});
        assertNull(osgiHttpRoutePlanner.determineProxyFromProxyConfiguration(httpHost, proxyConfigurationMock));

        when(proxyConfigurationMock.getProxyExceptions()).thenReturn(new String[]{".google.com", ".google.ru"});
        assertNotNull(osgiHttpRoutePlanner.determineProxyFromProxyConfiguration(httpHost, proxyConfigurationMock));

        when(proxyConfigurationMock.isEnabled()).thenReturn(false);
        assertNull(osgiHttpRoutePlanner.determineProxyFromProxyConfiguration(httpHost, proxyConfigurationMock));
    }
}
