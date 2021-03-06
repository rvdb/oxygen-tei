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
package ro.sync.ecss.extensions.commons.sort;

import java.util.List;

import ro.sync.annotations.api.API;
import ro.sync.annotations.api.APIType;
import ro.sync.annotations.api.SourceType;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.ContentInterval;
import ro.sync.ecss.extensions.api.SelectionInterpretationMode;
import ro.sync.ecss.extensions.api.access.AuthorEditorAccess;
import ro.sync.ecss.extensions.api.node.AuthorElement;

/**
 * Util class for table sort operations.
 */
@API(type=APIType.INTERNAL, src=SourceType.PUBLIC)
public class TableSortUtil {

  /**
   * Checks if the given element is entirely included in the current selection.
   * 
   * @param authorAccess    The author access.
   * @param element         The element to be checked.
   *                    
   * @return  <code>true</code> if the given element is entirely included
   *          in the current selection. The check is done only if the table 
   *          has COLUMN selection or TABLE selection.
   */
  public static boolean isEntirelySelected(AuthorAccess authorAccess, AuthorElement element) {
    boolean isSelected = false;
    AuthorEditorAccess editorAccess = authorAccess.getEditorAccess();
    if (editorAccess.getAuthorSelectionModel().hasSelection()) {
      // Check the selection
      SelectionInterpretationMode selectionInterpretationMode = editorAccess.getAuthorSelectionModel().getSelectionInterpretationMode();
      if (selectionInterpretationMode == SelectionInterpretationMode.TABLE) {
        // Table selection
        isSelected = true;
      } else if (selectionInterpretationMode == SelectionInterpretationMode.TABLE_COLUMN){
        isSelected = isIncludedInSelectionInterval(authorAccess, element);
      }
    }
    
    return isSelected;
  }
  
  /**
   * Checks if the given element is included in the selection.
   * 
   * @param authorAccess  The author access.
   * @param element       The element to be checked.
   * 
   * @return <code>true</code> if it is entirely included in the selection, 
   * <code>false</code> otherwise.
   */
  public static boolean isIncludedInSelectionInterval(AuthorAccess authorAccess, AuthorElement element) {
    List<ContentInterval> selectionIntervals = authorAccess.getEditorAccess().getAuthorSelectionModel().getSelectionIntervals();
    for (int i = 0; i < selectionIntervals.size(); i++) {
      ContentInterval contentInterval = selectionIntervals.get(i);
      if (contentInterval.getStartOffset() <= element.getStartOffset() && contentInterval.getEndOffset() >= element.getEndOffset()) {
        return true;
      }
    }
    
    return false;
  }

  /**
   * Checks if the current selection from the table is a {@link SelectionInterpretationMode#TABLE_COLUMN} 
   * selection or a {@link SelectionInterpretationMode#TABLE} selection.
   *  
   * @param authorAccess The author access.
   * 
   * @return <code>true</code> if the current selection is a {@link SelectionInterpretationMode#TABLE_COLUMN} 
   * selection or a {@link SelectionInterpretationMode#TABLE} selection.
   */
  public static boolean isColumnOrTableSelection(AuthorAccess authorAccess) {
    // Maybe we have an entire column selected or the entire table is selected, 
    // check the selection type
    SelectionInterpretationMode selectionInterpretationMode = authorAccess.getEditorAccess().getAuthorSelectionModel().getSelectionInterpretationMode();
    // All the table should be sorted because all the rows are selected
    // So there is no need to obtain the selected rows
    return selectionInterpretationMode == SelectionInterpretationMode.TABLE_COLUMN 
        || selectionInterpretationMode == SelectionInterpretationMode.TABLE;
  }
}
