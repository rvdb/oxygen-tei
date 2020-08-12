/*
 *  The Syncro Soft SRL License
 *
 *  Copyright (c) 1998-2012 Syncro Soft SRL, Romania.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistribution of source or in binary form is allowed only with
 *  the prior written permission of Syncro Soft SRL.
 *
 *  2. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  3. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  4. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Syncro Soft SRL (http://www.sync.ro/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  5. The names "Oxygen" and "Syncro Soft SRL" must
 *  not be used to endorse or promote products derived from this
 *  software without prior written permission. For written
 *  permission, please contact support@oxygenxml.com.
 *
 *  6. Products derived from this software may not be called "Oxygen",
 *  nor may "Oxygen" appear in their name, without prior written
 *  permission of the Syncro Soft SRL.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE SYNCRO SOFT SRL OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 */
package ro.sync.ecss.extensions.commons.table.operations;

import java.io.File;

import ro.sync.ecss.component.ui.actions.edit.CutOrCopyAction;
import ro.sync.ecss.extensions.EditorAuthorExtensionTestBase;
import ro.sync.exml.editor.xmleditor.pageauthor.AuthorEditorPage;
import ro.sync.exml.view.ViewportMouseEvent;
import ro.sync.exml.view.graphics.Rectangle;
import ro.sync.ui.UiUtil;
import ro.sync.util.URLUtil;

/**
 * Tests for delete rows for DITA, Docbook, TEi and XHTML frameworks.
 * 
 * @author radu_coravu
 */
public class DeleteTableRowTest extends EditorAuthorExtensionTestBase {

  /**
   * @see ro.sync.ecss.extensions.EditorAuthorExtensionTestBase#setUp()
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  /**
   * <p><b>Description:</b> Test delete row from an author table for DITA.</p>
   * <p><b>Bug ID:</b> EXM-23338</p>
   *
   * @author radu_coravu
   *
   * @throws Exception
   */
  public void testDeleteTableRowDITA() throws Exception {
    open(URLUtil.correct(new File("test/EXM-18945/testTable.dita")), true);

    final AuthorEditorPage authorPage = 
        (AuthorEditorPage)editorManager.getSelectedEditor().getCurrentPage();
    
    // Move the mouse over the first row
    moveCaretRelativeTo("Column 1", 0);
    Rectangle caret = authorPage.getViewport().getCaretShape();
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);
    
    final CutOrCopyAction cut = new CutOrCopyAction(vViewport, true);
    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
    
    String expectedResult1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<?xml-stylesheet type=\"text/css\" href=\"../../frameworks/dita/css_classed/hide_colspec.css\" ?>\n" + 
    "<!DOCTYPE task PUBLIC \"-//OASIS//DTD DITA General Task//EN\" \"task.dtd\">\n" + 
    "<task id=\"task_kwd_zgb_ff\">\n" + 
    "    <title>Task title</title>\n" + 
    "    <shortdesc/>\n" + 
    "    <taskbody>\n" + 
    "        <context>\n" + 
    "            <p>Context<table frame=\"all\" id=\"table_vp2_1hb_ff\">\n" + 
    "                    <title/>\n" + 
    "                    <tgroup cols=\"2\">\n" + 
    "                        <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.0*\"/>\n" + 
    "                        <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1.0*\"/>\n" + 
    "                        <tbody>\n" + 
    "                            <row>\n" + 
    "                                <entry>1</entry>\n" + 
    "                                <entry>4</entry>\n" + 
    "                            </row>\n" + 
    "                            <row>\n" + 
    "                                <entry>2</entry>\n" + 
    "                                <entry>5</entry>\n" + 
    "                            </row>\n" + 
    "                            <row>\n" + 
    "                                <entry>3</entry>\n" + 
    "                                <entry>6</entry>\n" + 
    "                            </row>\n" + 
    "                        </tbody>\n" + 
    "                    </tgroup>\n" + 
    "                </table> for the current task</p>\n" + 
    "        </context>\n" + 
    "    </taskbody>\n" + 
    "</task>\n" + 
    "";
    
    String result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult1, result);
    
    // Move the mouse over the first row
    moveCaretRelativeTo("1", 0);
    caret = authorPage.getViewport().getCaretShape();
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);
    
    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
    
    String expectedResult2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<?xml-stylesheet type=\"text/css\" href=\"../../frameworks/dita/css_classed/hide_colspec.css\" ?>\n" + 
    "<!DOCTYPE task PUBLIC \"-//OASIS//DTD DITA General Task//EN\" \"task.dtd\">\n" + 
    "<task id=\"task_kwd_zgb_ff\">\n" + 
    "    <title>Task title</title>\n" + 
    "    <shortdesc/>\n" + 
    "    <taskbody>\n" + 
    "        <context>\n" + 
    "            <p>Context<table frame=\"all\" id=\"table_vp2_1hb_ff\">\n" + 
    "                    <title/>\n" + 
    "                    <tgroup cols=\"2\">\n" + 
    "                        <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.0*\"/>\n" + 
    "                        <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1.0*\"/>\n" + 
    "                        <tbody>\n" + 
    "                            <row>\n" + 
    "                                <entry>2</entry>\n" + 
    "                                <entry>5</entry>\n" + 
    "                            </row>\n" + 
    "                            <row>\n" + 
    "                                <entry>3</entry>\n" + 
    "                                <entry>6</entry>\n" + 
    "                            </row>\n" + 
    "                        </tbody>\n" + 
    "                    </tgroup>\n" + 
    "                </table> for the current task</p>\n" + 
    "        </context>\n" + 
    "    </taskbody>\n" + 
    "</task>\n" + 
    "";
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult2, result);
    
    doUndo();
    
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult1, result);
    
    doRedo();
    
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult2, result);
  }

  /**
   * <p><b>Description:</b> Test delete row from an author table for Docbook4.</p>
   * <p><b>Bug ID:</b> EXM-23338</p>
   *
   * @author radu_coravu
   *
   * @throws Exception
   */
  public void testDeleteTableRowDocbook4() throws Exception {
    open(URLUtil.correct(new File("test/EXM-18945/testTableDocbook.xml")), true);
  
    final AuthorEditorPage authorPage = 
        (AuthorEditorPage)editorManager.getSelectedEditor().getCurrentPage();
    
    // Move the mouse over the first row
    moveCaretRelativeTo("Column 1", 0);
    Rectangle caret = authorPage.getViewport().getCaretShape();
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 5, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 5, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);
    
    String selectedText = vViewport.getSelectedText();
    assertEquals("Column 1Column 2", selectedText);
    
    final CutOrCopyAction cut = new CutOrCopyAction(vViewport, true);
    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
  
    String expectedResult1 = 
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<?xml-stylesheet type=\"text/css\" href=\"../../frameworks/docbook/css/hide_colspec.css\"?>\n" +
    "<!DOCTYPE article PUBLIC \"-//OASIS//DTD DocBook XML V4.5//EN\"\n" + 
    "                         \"http://www.docbook.org/xml/4.5/docbookx.dtd\">\n" + 
    "<article>\n" + 
    "    <title>Article Title</title>\n" + 
    "    <sect1>\n" + 
    "        <title>Section1 Title</title>\n" + 
    "        <para>Text<table frame=\"void\">\n" + 
    "                <caption/>\n" + 
    "                <col width=\"50%\"/>\n" + 
    "                <col width=\"50%\"/>\n" + 
    "                <tbody>\n" + 
    "                    <tr>\n" + 
    "                        <td>1</td>\n" + 
    "                        <td>4</td>\n" + 
    "                    </tr>\n" + 
    "                    <tr>\n" + 
    "                        <td>2</td>\n" + 
    "                        <td>5</td>\n" + 
    "                    </tr>\n" + 
    "                    <tr>\n" + 
    "                        <td>3</td>\n" + 
    "                        <td>6</td>\n" + 
    "                    </tr>\n" + 
    "                </tbody>\n" + 
    "            </table></para>\n" + 
    "    </sect1>\n" + 
    "</article>";
    String result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult1, result);
    
    // Move the mouse over the second row
    moveCaretRelativeTo("2", 0);
    caret = authorPage.getViewport().getCaretShape();
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);

    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
  
    String expectedResult2 = 
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<?xml-stylesheet type=\"text/css\" href=\"../../frameworks/docbook/css/hide_colspec.css\"?>\n" + 
    "<!DOCTYPE article PUBLIC \"-//OASIS//DTD DocBook XML V4.5//EN\"\n" + 
    "                         \"http://www.docbook.org/xml/4.5/docbookx.dtd\">\n" + 
    "<article>\n" + 
    "    <title>Article Title</title>\n" + 
    "    <sect1>\n" + 
    "        <title>Section1 Title</title>\n" + 
    "        <para>Text<table frame=\"void\">\n" + 
    "                <caption/>\n" + 
    "                <col width=\"50%\"/>\n" + 
    "                <col width=\"50%\"/>\n" + 
    "                <tbody>\n" + 
    "                    <tr>\n" + 
    "                        <td>1</td>\n" + 
    "                        <td>4</td>\n" + 
    "                    </tr>\n" + 
    "                    <tr>\n" + 
    "                        <td>3</td>\n" + 
    "                        <td>6</td>\n" + 
    "                    </tr>\n" + 
    "                </tbody>\n" + 
    "            </table></para>\n" + 
    "    </sect1>\n" + 
    "</article>";
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult2, result);
    
    doUndo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult1, result);
    
    doRedo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult2, result);
  }

  /**
   * <p><b>Description:</b> Test delete row from an author table for XHTML.</p>
   * <p><b>Bug ID:</b> EXM-23338</p>
   *
   * @author radu_coravu
   *
   * @throws Exception
   */
  public void testDeleteTableRowXHTML() throws Exception {
    open(URLUtil.correct(new File("test/EXM-18945/testTableXHTML.xhtml")), true);
  
    final AuthorEditorPage authorPage = 
        (AuthorEditorPage)editorManager.getSelectedEditor().getCurrentPage();
    
    // Move the mouse over the first row
    moveCaretRelativeTo("Column 1", 0);
    Rectangle caret = authorPage.getViewport().getCaretShape();
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 5, caret.y + 10, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 5, caret.y + 10, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);
    
    final CutOrCopyAction cut = new CutOrCopyAction(vViewport, true);
    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
    
    String expectedResult1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" + 
    "                      \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + 
    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
    "    <head>\n" + 
    "        <title></title>\n" + 
    "    </head>\n" + 
    "    <body>\n" + 
    "        <p>Text</p>\n" + 
    "        <table frame=\"void\">\n" + 
    "            <caption></caption>\n" + 
    "            <col width=\"50%\" />\n" + 
    "            <col width=\"50%\" />\n" + 
    "            <tbody>\n" + 
    "                <tr>\n" + 
    "                    <td>1</td>\n" + 
    "                    <td>4</td>\n" + 
    "                </tr>\n" + 
    "                <tr>\n" + 
    "                    <td>2</td>\n" + 
    "                    <td>5</td>\n" + 
    "                </tr>\n" + 
    "                <tr>\n" + 
    "                    <td>3</td>\n" + 
    "                    <td>6</td>\n" + 
    "                </tr>\n" + 
    "            </tbody>\n" + 
    "        </table>\n" + 
    "    </body>\n" + 
    "</html>";
    String result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult1, result);
    
    // Move the mouse over the first row
    moveCaretRelativeTo("1", 0);
    caret = authorPage.getViewport().getCaretShape();
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 5, caret.y + 10, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 5, caret.y + 10, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);
    
    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
    
    String expectedResult2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" + 
    "                      \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + 
    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
    "    <head>\n" + 
    "        <title></title>\n" + 
    "    </head>\n" + 
    "    <body>\n" + 
    "        <p>Text</p>\n" + 
    "        <table frame=\"void\">\n" + 
    "            <caption></caption>\n" + 
    "            <col width=\"50%\" />\n" + 
    "            <col width=\"50%\" />\n" + 
    "            <tbody>\n" + 
    "                <tr>\n" + 
    "                    <td>2</td>\n" + 
    "                    <td>5</td>\n" + 
    "                </tr>\n" + 
    "                <tr>\n" + 
    "                    <td>3</td>\n" + 
    "                    <td>6</td>\n" + 
    "                </tr>\n" + 
    "            </tbody>\n" + 
    "        </table>\n" + 
    "    </body>\n" + 
    "</html>";
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult2, result);
    
    doUndo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult1, result);
    
    doRedo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult2, result);
  }

  /**
   * <p><b>Description:</b> Test delete row from an author table for TEI.</p>
   * <p><b>Bug ID:</b> EXM-23338</p>
   *
   * @author radu_coravu
   *
   * @throws Exception
   */
  public void testDeleteTableRowTEI() throws Exception {
    open(URLUtil.correct(new File("test/EXM-18945/testTableTEI.xml")), true);
  
    final AuthorEditorPage authorPage = 
        (AuthorEditorPage)editorManager.getSelectedEditor().getCurrentPage();
    
    // Move the mouse over the first row
    moveCaretRelativeTo("Column 1", 0);
    Rectangle caret = authorPage.getViewport().getCaretShape();
    
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);
    
    final CutOrCopyAction cut = new CutOrCopyAction(vViewport, true);
    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
  
    String expectedResult1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
    "<?xml-model href=\"http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_lite.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>\n" + 
    "<TEI xmlns=\"http://www.tei-c.org/ns/1.0\">\n" + 
    "  <teiHeader>\n" + 
    "    <fileDesc>\n" + 
    "      <titleStmt>\n" + 
    "        <title>Title</title>\n" + 
    "      </titleStmt>\n" + 
    "      <publicationStmt>\n" + 
    "        <p>Publication information</p>        \n" + 
    "      </publicationStmt>\n" + 
    "      <sourceDesc>\n" + 
    "        <p>Information about the source</p>\n" + 
    "      </sourceDesc>\n" + 
    "    </fileDesc>\n" + 
    "  </teiHeader>\n" + 
    "  <text>\n" + 
    "    <body>\n" + 
    "      <p>Some text here.<table rows=\"2\" cols=\"2\">\n" + 
    "                    <head/>\n" + 
    "                    <row>\n" + 
    "                        <cell>1</cell>\n" + 
    "                        <cell>4</cell>\n" + 
    "                    </row>\n" + 
    "                    <row>\n" + 
    "                        <cell>2</cell>\n" + 
    "                        <cell>5</cell>\n" + 
    "                    </row>\n" + 
    "                    <row>\n" + 
    "                        <cell>3</cell>\n" + 
    "                        <cell>6</cell>\n" + 
    "                    </row>\n" + 
    "                </table></p>\n" + 
    "    </body>\n" + 
    "  </text>\n" + 
    "</TEI>";
    String result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult1, result);
    
    // Move the mouse over the second row
    moveCaretRelativeTo("2", 0);
    caret = authorPage.getViewport().getCaretShape();
        authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 10, caret.y + 10, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);

    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
  
    String expectedResult2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
    "<?xml-model href=\"http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_lite.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>\n" + 
    "<TEI xmlns=\"http://www.tei-c.org/ns/1.0\">\n" + 
    "  <teiHeader>\n" + 
    "    <fileDesc>\n" + 
    "      <titleStmt>\n" + 
    "        <title>Title</title>\n" + 
    "      </titleStmt>\n" + 
    "      <publicationStmt>\n" + 
    "        <p>Publication information</p>        \n" + 
    "      </publicationStmt>\n" + 
    "      <sourceDesc>\n" + 
    "        <p>Information about the source</p>\n" + 
    "      </sourceDesc>\n" + 
    "    </fileDesc>\n" + 
    "  </teiHeader>\n" + 
    "  <text>\n" + 
    "    <body>\n" + 
    "      <p>Some text here.<table rows=\"1\" cols=\"2\">\n" + 
    "                    <head/>\n" + 
    "                    <row>\n" + 
    "                        <cell>1</cell>\n" + 
    "                        <cell>4</cell>\n" + 
    "                    </row>\n" + 
    "                    <row>\n" + 
    "                        <cell>3</cell>\n" + 
    "                        <cell>6</cell>\n" + 
    "                    </row>\n" + 
    "                </table></p>\n" + 
    "    </body>\n" + 
    "  </text>\n" + 
    "</TEI>";
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult2, result);
    
    doUndo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult1, result);
    
    doRedo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult2, result);
  }

  /**
   * <p><b>Description:</b> Test delete row from an author table with row span for XHTML.</p>
   * <p><b>Bug ID:</b> EXM-23338</p>
   *
   * @author radu_coravu
   *
   * @throws Exception
   */
  public void testDeleteTableRowWithSpanXHTML() throws Exception {
    open(URLUtil.correct(new File("test/EXM-23338/xhtmltable.xhtml")), true);
  
    final AuthorEditorPage authorPage = 
        (AuthorEditorPage)editorManager.getSelectedEditor().getCurrentPage();
    
    // Move the mouse over the second row
    moveCaretRelativeTo("R1", 0);
    Rectangle caret = authorPage.getViewport().getCaretShape();
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 5, caret.y + 25, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 5, caret.y + 25, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);
    
    final CutOrCopyAction cut = new CutOrCopyAction(vViewport, true);
    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
    
    String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" + 
    "                      \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + 
    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
    "    <head>\n" + 
    "        <title></title>\n" + 
    "    </head>\n" + 
    "    <body>\n" + 
    "        <table frame=\"void\">\n" + 
    "            <caption></caption>\n" + 
    "            <col width=\"50%\" />\n" + 
    "            <col width=\"50%\" />\n" + 
    "            <thead>\n" + 
    "                <tr>\n" + 
    "                    <th>Column1</th>\n" + 
    "                    <th>Column2</th>\n" + 
    "                </tr>\n" + 
    "            </thead>\n" + 
    "            <tbody>\n" + 
    "                <tr>\n" + 
    "                    <td>R1</td>\n" + 
    "                    <td>3</td>\n" + 
    "                </tr>\n" + 
    "                <tr>\n" + 
    "                    <td>R3</td>\n" + 
    "                    <td>5</td>\n" + 
    "                </tr>\n" + 
    "            </tbody>\n" + 
    "        </table>\n" + 
    "    </body>\n" + 
    "</html>";
    String result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult, result);
    
    doUndo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
    		"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" + 
    		"                      \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + 
    		"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
    		"    <head>\n" + 
    		"        <title></title>\n" + 
    		"    </head>\n" + 
    		"    <body>\n" + 
    		"        <table frame=\"void\">\n" + 
    		"            <caption></caption>\n" + 
    		"            <col width=\"50%\" />\n" + 
    		"            <col width=\"50%\" />\n" + 
    		"            <thead>\n" + 
    		"                <tr>\n" + 
    		"                    <th>Column1</th>\n" + 
    		"                    <th>Column2</th>\n" + 
    		"                </tr>\n" + 
    		"            </thead>\n" + 
    		"            <tbody>\n" + 
    		"                <tr>\n" + 
    		"                    <td rowspan=\"2\">R1</td>\n" + 
    		"                    <td>3</td>\n" + 
    		"                </tr>\n" + 
    		"                <tr>\n" + 
    		"                    <td>4</td>\n" + 
    		"                </tr>\n" + 
    		"                <tr>\n" + 
    		"                    <td>R3</td>\n" + 
    		"                    <td>5</td>\n" + 
    		"                </tr>\n" + 
    		"            </tbody>\n" + 
    		"        </table>\n" + 
    		"    </body>\n" + 
    		"</html>", result);
    
    doRedo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult, result);
  }

  /**
   * <p><b>Description:</b> Test delete row from an author table with row span for TEI.</p>
   * <p><b>Bug ID:</b> EXM-23338</p>
   *
   * @author radu_coravu
   *
   * @throws Exception
   */
  public void testDeleteTableRowWithSpanTEI() throws Exception {
    open(URLUtil.correct(new File("test/EXM-23338/teiTable.xml")), true);
  
    final AuthorEditorPage authorPage = 
        (AuthorEditorPage)editorManager.getSelectedEditor().getCurrentPage();
    
    // Move the mouse over the second row
    moveCaretRelativeTo("R1", 0);
    Rectangle caret = authorPage.getViewport().getCaretShape();
        authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 28, caret.y + 30, 
        false, ViewportMouseEvent.STATE_MOVED, ViewportMouseEvent.BUTTON1, 1));
    authorPage.getViewport().mouseEvent(new ViewportMouseEvent(caret.x - 28, caret.y + 30, 
        false, ViewportMouseEvent.STATE_PRESSED, ViewportMouseEvent.BUTTON1, 1));
    Thread.sleep(500);

    final CutOrCopyAction cut = new CutOrCopyAction(vViewport, true);
    UiUtil.invokeSynchronously(new Runnable() {
      @Override
      public void run() {
        cut.actionPerformed(null);    
      }
    });
    flushAWTBetter();
    Thread.sleep(200);
    
    String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
    "<?xml-model href=\"http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_all.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>\n" + 
    "<TEI\n" + 
    "  xmlns:xi=\"http://www.w3.org/2001/XInclude\"\n" + 
    "  xmlns:svg=\"http://www.w3.org/2000/svg\"\n" + 
    "  xmlns:math=\"http://www.w3.org/1998/Math/MathML\"\n" + 
    "  xmlns=\"http://www.tei-c.org/ns/1.0\">\n" + 
    "  <teiHeader>\n" + 
    "    <fileDesc>\n" + 
    "      <titleStmt>\n" + 
    "        <title>Title</title>\n" + 
    "      </titleStmt>\n" + 
    "      <publicationStmt>\n" + 
    "        <p>Publication Information</p>\n" + 
    "      </publicationStmt>\n" + 
    "      <sourceDesc>\n" + 
    "        <p>Information about the source</p>\n" + 
    "      </sourceDesc>\n" + 
    "    </fileDesc>\n" + 
    "  </teiHeader>\n" + 
    "  <text>\n" + 
    "    <body>\n" + 
    "      <p>Some text here.<table rows=\"3\" cols=\"2\">\n" + 
    "                    <head/>\n" + 
    "                    <row role=\"label\">\n" + 
    "                        <cell>Column1</cell>\n" + 
    "                        <cell>Column2</cell>\n" + 
    "                    </row>\n" + 
    "                    <row>\n" + 
    "                        <cell>R1</cell>\n" + 
    "                        <cell>1</cell>\n" + 
    "                    </row>\n" + 
    "                    <row>\n" + 
    "                        <cell>R2</cell>\n" + 
    "                        <cell>3</cell>\n" + 
    "                    </row>\n" + 
    "                    <row>\n" + 
    "                        <cell>R3</cell>\n" + 
    "                        <cell>4</cell>\n" + 
    "                    </row>\n" + 
    "                </table></p>\n" + 
    "    </body>\n" + 
    "  </text>\n" + 
    "</TEI>";
    String result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("Should have deleted selected row", 
        expectedResult, result);
    
    doUndo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
    		"<?xml-model href=\"http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_all.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>\n" + 
    		"<TEI\n" + 
    		"  xmlns:xi=\"http://www.w3.org/2001/XInclude\"\n" + 
    		"  xmlns:svg=\"http://www.w3.org/2000/svg\"\n" + 
    		"  xmlns:math=\"http://www.w3.org/1998/Math/MathML\"\n" + 
    		"  xmlns=\"http://www.tei-c.org/ns/1.0\">\n" + 
    		"  <teiHeader>\n" + 
    		"    <fileDesc>\n" + 
    		"      <titleStmt>\n" + 
    		"        <title>Title</title>\n" + 
    		"      </titleStmt>\n" + 
    		"      <publicationStmt>\n" + 
    		"        <p>Publication Information</p>\n" + 
    		"      </publicationStmt>\n" + 
    		"      <sourceDesc>\n" + 
    		"        <p>Information about the source</p>\n" + 
    		"      </sourceDesc>\n" + 
    		"    </fileDesc>\n" + 
    		"  </teiHeader>\n" + 
    		"  <text>\n" + 
    		"    <body>\n" + 
    		"      <p>Some text here.<table rows=\"4\" cols=\"2\">\n" + 
    		"          <head/>\n" + 
    		"          <row role=\"label\">\n" + 
    		"            <cell>Column1</cell>\n" + 
    		"            <cell>Column2</cell>\n" + 
    		"          </row>\n" + 
    		"          <row>\n" + 
    		"            <cell rows=\"2\">R1</cell>\n" + 
    		"            <cell>1</cell>\n" + 
    		"          </row>\n" + 
    		"          <row>\n" + 
    		"            <cell>2</cell>\n" + 
    		"          </row>\n" + 
    		"          <row>\n" + 
    		"            <cell>R2</cell>\n" + 
    		"            <cell>3</cell>\n" + 
    		"          </row>\n" + 
    		"          <row>\n" + 
    		"            <cell>R3</cell>\n" + 
    		"            <cell>4</cell>\n" + 
    		"          </row>\n" + 
    		"        </table></p>\n" + 
    		"    </body>\n" + 
    		"  </text>\n" + 
    		"</TEI>", result);
    
    doRedo();
    result = serializeDocumentViewport(authorPage.getViewport(), true);
    assertEquals(expectedResult, result);
  }
  
//  /**
//   * <p><b>Description:</b> Test delete row from an author table for Docbook5.</p>
//   * <p><b>Bug ID:</b> EXM-23338</p>
//   *
//   * @author radu_coravu
//   *
//   * @throws Exception
//   */
//  public void testDeleteTableRowDocbook5() throws Exception {
//    open(URLUtil.correct(new File("test/EXM-18945/testTableDocbook5.xml")));
//  
//    final AuthorEditorPage authorPage = 
//        (AuthorEditorPage)editorManager.getSelectedEditor().getCurrentPage();
//    
//    // Move the mouse over the first row
//    moveCaretRelativeTo("Column 1", 0);
//    Rectangle caret = authorPage.getViewport().getCaretShape();
//    int caretX = caret.x - 10;
//    if (PlatformDetector.isLinux()) {
//      caretX = caret.x - 15;
//    }
//    MouseEventData srcEvtData = new MouseEventData(
//        this, authorPage.getSwingComponent(), 1, MouseEvent.BUTTON1_MASK, false, 
//        1000, new Point(caretX, caret.y + 10));
//    robotAWTHelper.enterClickAndLeave(srcEvtData);
//    
//    final CutOrCopyAction cut = new CutOrCopyAction(vViewport, true);
//    UiUtil.invokeSynchronously(new Runnable() {
//      @Override
//      public void run() {
//        cut.actionPerformed(null);    
//      }
//    });
//    flushAWTBetter();
//    Thread.sleep(200);
//  
//    String expectedResult1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//    "<?xml-model href=\"http://www.oasis-open.org/docbook/xml/5.0/rng/docbook.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>\n" + 
//    "<article xmlns=\"http://docbook.org/ns/docbook\"\n" + 
//    "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"5.0\">\n" + 
//    "    <info>\n" + 
//    "        <title>Article Template Title</title>\n" + 
//    "    </info>\n" + 
//    "    <sect1>\n" + 
//    "        <title>Section1 Title</title>\n" + 
//    "        <subtitle>Section1 Subtitle</subtitle>\n" + 
//    "        <para>Text<table frame=\"all\">\n" + 
//    "                <title/>\n" + 
//    "                <tgroup cols=\"2\">\n" + 
//    "                    <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.0*\"/>\n" + 
//    "                    <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1.0*\"/>\n" + 
//    "                    <thead/>\n" + 
//    "                    <tbody>\n" + 
//    "                        <row>\n" + 
//    "                            <entry>1</entry>\n" + 
//    "                            <entry>4</entry>\n" + 
//    "                        </row>\n" + 
//    "                        <row>\n" + 
//    "                            <entry>2</entry>\n" + 
//    "                            <entry>5</entry>\n" + 
//    "                        </row>\n" + 
//    "                        <row>\n" + 
//    "                            <entry>3</entry>\n" + 
//    "                            <entry>6</entry>\n" + 
//    "                        </row>\n" + 
//    "                    </tbody>\n" + 
//    "                </tgroup>\n" + 
//    "            </table></para>\n" + 
//    "    </sect1>\n" + 
//    "</article>";
//    String result = serializeDocumentViewport(authorPage.getViewport(), true);
//    assertEquals("Should have deleted selected row", 
//        expectedResult1, result);
//    
//    moveCaretRelativeTo("2", 0);
//    caret = authorPage.getViewport().getCaretShape();
//    caretX = caret.x - 10;
//    if (PlatformDetector.isLinux()) {
//      caretX = caret.x - 15;
//    }
//    srcEvtData = new MouseEventData(
//        this, authorPage.getSwingComponent(), 1, MouseEvent.BUTTON1_MASK, false, 
//        1000, new Point(caretX, caret.y + 10));
//    robotAWTHelper.enterClickAndLeave(srcEvtData);
//    
//    UiUtil.invokeSynchronously(new Runnable() {
//      @Override
//      public void run() {
//        cut.actionPerformed(null);    
//      }
//    });
//    flushAWTBetter();
//    Thread.sleep(200);
//  
//    String expectedResult2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//    "<?xml-model href=\"http://www.oasis-open.org/docbook/xml/5.0/rng/docbook.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>\n" + 
//    "<article xmlns=\"http://docbook.org/ns/docbook\"\n" + 
//    "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"5.0\">\n" + 
//    "    <info>\n" + 
//    "        <title>Article Template Title</title>\n" + 
//    "    </info>\n" + 
//    "    <sect1>\n" + 
//    "        <title>Section1 Title</title>\n" + 
//    "        <subtitle>Section1 Subtitle</subtitle>\n" + 
//    "        <para>Text<table frame=\"all\">\n" + 
//    "                <title/>\n" + 
//    "                <tgroup cols=\"2\">\n" + 
//    "                    <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.0*\"/>\n" + 
//    "                    <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1.0*\"/>\n" + 
//    "                    <thead/>\n" + 
//    "                    <tbody>\n" + 
//    "                        <row>\n" + 
//    "                            <entry>1</entry>\n" + 
//    "                            <entry>4</entry>\n" + 
//    "                        </row>\n" + 
//    "                        <row>\n" + 
//    "                            <entry>3</entry>\n" + 
//    "                            <entry>6</entry>\n" + 
//    "                        </row>\n" + 
//    "                    </tbody>\n" + 
//    "                </tgroup>\n" + 
//    "            </table></para>\n" + 
//    "    </sect1>\n" + 
//    "</article>";
//    result = serializeDocumentViewport(authorPage.getViewport(), true);
//    assertEquals("Should have deleted selected row", 
//        expectedResult2, result);
//    
//    doUndo();
//    result = serializeDocumentViewport(authorPage.getViewport(), true);
//    assertEquals(expectedResult1, result);
//    
//    doRedo();
//    result = serializeDocumentViewport(authorPage.getViewport(), true);
//    assertEquals(expectedResult2, result);
//  }
}
