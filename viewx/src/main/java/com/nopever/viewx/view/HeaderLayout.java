package com.nopever.viewx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nopever.viewx.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundRelativeLayout;

/**
 * - ......................我佛慈悲....................
 * - .......................oo0oo.....................
 * - .....................o8888888o....................
 * - .....................88" . "88....................
 * - .....................(| -_- |)....................
 * - .....................0\  =  /0....................
 * - ..................._/`---'_..................
 * - ..................' \|     |// '.................
 * - ................./ \|||  :  |||// ..............
 * - .............../ _||||| -卍-|||||- ..............
 * - ..............|   | \\  -  /// |   |.............
 * - ..............| _|  ''---/''  |_/ |.............
 * - ..............\  .-__  '-'  ___/-. /.............
 * - ............'. .'  /--.--\  `. .'...........
 * - .........."" '<  `._<|>/_.' >' ""..........
 * - ........| | :  - \.;\ _ /;./ -  : | |.......
 * - ........\  \ _.   \_ __\ /__ _/   .- /  /.......
 * - ....=====-.____.___ _/.-`__.-'=====....
 * - ......................`=---='.....................
 * - ..................佛祖开光 ,永无BUG................
 * -
 * - @ProjectName android-registration
 * - @ClassName HeaderLayout
 * - @Package com.nopever.viewx.view
 * - @Description 头布局自定义
 * - @Author YCk
 * - @Date 2025/11/18 17:02
 * - @Version 1.0
 **/
public class HeaderLayout extends RelativeLayout {

    private LayoutInflater mInflater;
    private View mHeader;
    private LinearLayout linearHeaderRight;
    private ImageView titleLeftView, titleRightView;
    private TextView titleCenterView;

    private QMUIRoundRelativeLayout header_layout_right;
    private QMUIRoundButton btn_right_text;

    private OnLeftClickListener mOnLeftClickListener;
    private OnMiddleClickListener mOnMiddleClickListener;
    private OnRightClickListener mOnRightClickListener;

    private String right_text;
    private String center_text;
    private Drawable left_img;
    private Drawable right_img;
    private LinearLayout relative_root;
    private int color;
    private int center_textcolor;

    public HeaderLayout(Context context) {
        super(context);
        init(context);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HeaderLayout);
        right_text = (String) array.getText(R.styleable.HeaderLayout_hl_right_text);
        center_text = (String) array.getText(R.styleable.HeaderLayout_hl_center_text);
        left_img = array.getDrawable(R.styleable.HeaderLayout_hl_left_image);
        right_img = array.getDrawable(R.styleable.HeaderLayout_hl_right_image);
        color = array.getColor(R.styleable.HeaderLayout_hl_head_back, 0x00000000);
        center_textcolor = array.getColor(R.styleable.HeaderLayout_hl_center_textcolor, 0xFF4a4a4a);
        array.recycle();
        init(context);
    }


    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
        mHeader = mInflater.inflate(R.layout.hl_common_header_layout, null);
        addView(mHeader);
        initViews();
    }

    private void initViews() {
        relative_root = findViewById(R.id.relative_root);
        linearHeaderRight = findViewById(R.id.linearRightView);
        titleLeftView = findViewById(R.id.titleLeftView);
        titleRightView = findViewById(R.id.titleRightView);
        titleCenterView = findViewById(R.id.titleCenterView);
        header_layout_right = findViewById(R.id.header_layout_right);
        btn_right_text = findViewById(R.id.btn_right_text);

        if (0xFFFFFFFF != color) {
            relative_root.setBackgroundColor(color);
        }

        //左边图片
        if (null != left_img) {
            titleLeftView.setImageDrawable(left_img);
        }
        //中间文字
        if (null != center_text) {
            titleCenterView.setText(center_text);
            titleCenterView.setTextColor(center_textcolor);
        }
        //右边文字
        if (null != right_text) {
            header_layout_right.setVisibility(View.VISIBLE);
            btn_right_text.setText(right_text);
        } else {
            header_layout_right.setVisibility(View.GONE);
        }
        //右边图片
        if (null != right_img) {
            titleRightView.setVisibility(View.VISIBLE);
            titleRightView.setImageDrawable(right_img);
        } else {
            titleRightView.setVisibility(View.GONE);
        }
    }

    public View findViewByHeaderId(int id) {
        return mHeader.findViewById(id);
    }

    public void setMidText(String text) {
        titleCenterView.setText(text);
    }

    public void setLeftImage(int id) {
        titleLeftView.setImageDrawable(getResources().getDrawable(id));
        titleLeftView.setVisibility(VISIBLE);
    }

    public void setRightImage(int id) {
        titleRightView.setImageDrawable(getResources().getDrawable(id));
        titleRightView.setVisibility(VISIBLE);
    }

    public void setRightText(String text) {
        if (TextUtils.isEmpty(text)) {
            header_layout_right.setVisibility(GONE);
        } else {
            header_layout_right.setVisibility(VISIBLE);
        }
        btn_right_text.setText(text);
    }

    public void setOnLeftImageViewClickListener(OnLeftClickListener listener) {
        setLeftClick(listener);
    }

    public void setOnMiddleTextViewClickListener(OnMiddleClickListener listener) {
        setMiddleClick(listener);
    }

    public void setOnRightImageViewClickListener(OnRightClickListener listener) {
        setRightClick(listener);
    }

    public interface OnLeftClickListener {
        void onClick();
    }

    public interface OnMiddleClickListener {
        void onClick();
    }

    public interface OnRightClickListener {
        void onClick();
    }

    /**
     * 设置左边和中间信息
     *
     * @param leftClick
     * @param titleCenter
     */
    public void setLeftCenter(OnLeftClickListener leftClick, String titleCenter) {
        setLeftClick(leftClick);
        titleCenterView.setText(titleCenter);
    }

    /**
     * 设置左,中(文字)，右(设置)
     *
     * @param leftClick
     * @param titleCenter
     * @param rightClick
     */
    public void setLeftCenterSet(OnLeftClickListener leftClick, String titleCenter, OnRightClickListener rightClick) {
        setLeftClick(leftClick);
        titleCenterView.setText(titleCenter);
        setRightClick(rightClick);
    }

    /**
     * 设置左,中(下拉对话框)，右(刷新)
     *
     * @param leftClick
     * @param middleClick
     * @param rightClick
     */
    public void setLeftCenterRight(OnLeftClickListener leftClick, OnMiddleClickListener middleClick, OnRightClickListener rightClick) {
        setLeftClick(leftClick);
        setMiddleClick(middleClick);
        setRightClick(rightClick);
    }

    /**
     * 设置左,中，右(刷新)
     *
     * @param leftClick
     * @param titleCenter
     * @param rightClick
     */
    public void setLeftCenterRight(OnLeftClickListener leftClick, String titleCenter, OnRightClickListener rightClick) {
        titleCenterView.setText(titleCenter);
        setLeftClick(leftClick);
        setRightClick(rightClick);
    }

    /**
     * 设置左，中，右（文字信息）
     *
     * @param leftClick
     * @param titleCenter
     * @param rightText
     */
    public void setLeftCenterRight(OnLeftClickListener leftClick, String titleCenter, String rightText) {
        titleCenterView.setText(titleCenter);
        setLeftClick(leftClick);
        btn_right_text.setText(rightText);
    }

    private void setLeftClick(OnLeftClickListener leftClick) {
        mOnLeftClickListener = leftClick;
        titleLeftView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnLeftClickListener != null) {
                    mOnLeftClickListener.onClick();
                }
            }
        });
    }

    private void setMiddleClick(OnMiddleClickListener middleClick) {
        mOnMiddleClickListener = middleClick;
        titleCenterView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnMiddleClickListener != null) {
                    mOnMiddleClickListener.onClick();
                }
            }
        });
    }

    private void setRightClick(OnRightClickListener rightClick) {
        mOnRightClickListener = rightClick;
        linearHeaderRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnRightClickListener != null) {
                    mOnRightClickListener.onClick();
                }
            }
        });
    }
}
