package com.su.service.ui.artikel.buateditartikel

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import com.su.service.R
import net.dankito.richtexteditor.android.command.*
import net.dankito.richtexteditor.android.toolbar.EditorToolbar

open class CustomEditorToolbar : EditorToolbar{
    constructor(context: Context) : super(context) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initToolbar() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initToolbar() }


    protected open fun initToolbar() {
        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.ic_line)
        imageView.layoutParams = LayoutParams(2, MATCH_PARENT)
        addCommand(UndoCommand())
        addCommand(RedoCommand())
        addCommand(BoldCommand())
        addCommand(ItalicCommand())
        addCommand(UnderlineCommand())
        addCommand(InsertBulletListCommand())
        addCommand(InsertNumberedListCommand())
      //  addCommand(BlockQuoteCommand())
      //  addCommand(SetTextFormatCommand())
       // addCommand(SetFontNameWithPreviewCommand())
        addCommand(AlignLeftCommand())
        addCommand(AlignRightCommand())
        addCommand(AlignCenterCommand())
        addCommand(AlignJustifyCommand())
        addSearchView()
    }
}