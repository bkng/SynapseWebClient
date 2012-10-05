package org.sagebionetworks.web.unitclient;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.DisplayUtils;

public class DisplayUtilsTest {
	
	@Test
	public void testGetMimeType(){
		Map<String, String> expected = new HashMap<String, String>();
		expected.put("test.tar.gz", "gz");
		expected.put("test.txt", "txt");
		expected.put("test", null);
		expected.put("test.", null);
		for(String fileName: expected.keySet()){
			String expectedMime = expected.get(fileName);
			String mime = DisplayUtils.getMimeType(fileName);
			assertEquals(expectedMime, mime);
		}
	}
	
	@Test
	public void testGetIcon(){
		Map<String, String> expected = new HashMap<String, String>();
		String compressed = 
		expected.put("test.tar.GZ", DisplayUtils.DEFAULT_COMPRESSED_ICON);
		expected.put("test.doc", DisplayUtils.DEFAULT_TEXT_ICON);
		expected.put("test", DisplayUtils.UNKNOWN_ICON);
		expected.put("test.", DisplayUtils.UNKNOWN_ICON);
		expected.put("test.PDF", DisplayUtils.DEFAULT_PDF_ICON);
		expected.put("test.Zip", DisplayUtils.DEFAULT_COMPRESSED_ICON);
		expected.put("test.png", DisplayUtils.DEFAULT_IMAGE_ICON);
		for(String fileName: expected.keySet()){
			String expectedIcon = expected.get(fileName);
			String icon = DisplayUtils.getAttachmentIcon(fileName);
			assertEquals(expectedIcon, icon);
		}
	}
	
	@Test
	public void testFixWikiLinks(){
		String testHref = "Hello <a href=\"/wiki/HelloWorld.html\">World</a>";
		String expectedHref = "Hello <a href=\"https://sagebionetworks.jira.com/wiki/HelloWorld.html\">World</a>";
		String actualHref = DisplayUtils.fixWikiLinks(testHref);
		Assert.assertEquals(actualHref, expectedHref);
	}
	
	@Test
	public void testFixEmbeddedYouTube(){
		String testYouTube = "Hello video:<p> www.youtube.com/embed/xSfd5mkkmGM </p>";
		String expectedYouTube = "Hello video:<p> <iframe width=\"300\" height=\"169\" src=\"https://www.youtube.com/embed/xSfd5mkkmGM \" frameborder=\"0\" allowfullscreen=\"true\"></iframe></p>";
		String actualYouTube = DisplayUtils.fixEmbeddedYouTube(testYouTube);
		Assert.assertEquals(actualYouTube, expectedYouTube);
	}

	@Test
	public void testDetectEntityLinks(){
		String testString = "synapse123 SYn1234\nsyn567 syntax syn3 <a href=\"#Synapse:syn555\">syn555</a> syn";
		String expectedResult = "synapse123<a class=\"link\" href=\"#Synapse:syn1234\"> SYn1234\n</a><a class=\"link\" href=\"#Synapse:syn567\">syn567 </a>syntax<a class=\"link\" href=\"#Synapse:syn3\"> syn3 </a><a href=\"#Synapse:syn555\">syn555</a> syn";
		String actualResult = DisplayUtils.addSynapseLinks(testString);
		Assert.assertEquals(actualResult, expectedResult);
	}


	@Test
	public void testFixCSSClass(){
		String testString = "<ul><li>Abacus<ul><li>answer</li></ul></li><li>Bubbles<ol><li>bunk</li><li>bupkis<ul><li>BELITTLER</li></ul></li><li>burper</li></ol></li><li>Cunning</li></ul><blockquote> <p>Email-style angle brackets are used for blockquotes.</p></blockquote> <p><code>&lt;code&gt;</code> spans are delimited by backticks.</p><p>An <a href=\"http://url.com/\" title=\"Title\">example</a></p>";
		String expectedResult = "<ul class=\"myclass\"><li>Abacus<ul class=\"myclass\"><li>answer</li></ul></li><li>Bubbles<ol class=\"myclass\"><li>bunk</li><li>bupkis<ul class=\"myclass\"><li>BELITTLER</li></ul></li><li>burper</li></ol></li><li>Cunning</li></ul><blockquote class=\"myclass\"> <p>Email-style angle brackets are used for blockquotes.</p></blockquote> <p><code>&lt;code&gt;</code> spans are delimited by backticks.</p><p>An <a class=\"myclass\" href=\"http://url.com/\" title=\"Title\">example</a></p>";
		String actualResult = DisplayUtils.postProcessEntityDescriptionHtml(testString, "myclass");
		Assert.assertEquals(actualResult, expectedResult);
	}

}
