package com.project.numerojyotish.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.numerojyotish.Adapter.BasicChartAdapter;
import com.project.numerojyotish.Api.ApiClass;
import com.project.numerojyotish.Model.BasicChartDTO;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.FragmentBasicInfoBinding;
import com.project.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicInfoFragment extends Fragment {
    private BasicChartAdapter adapter;
    private ArrayList<BasicChartDTO> basicChartDTOArrayList;
    FragmentBasicInfoBinding binding;
    SessionManager sessionManager;

    public BasicInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basic_info, container, false);
        View view = binding.getRoot();

        initialize();
        return  view;
    }

    private void initialize() {

        sessionManager=  new SessionManager(getActivity().getApplicationContext());
        basicChartDTOArrayList = new ArrayList<>();
        adapter = new BasicChartAdapter(getActivity(), basicChartDTOArrayList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.scheduleLayoutAnimation();

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.vertical_divider);
        verticalDecoration.setDrawable(verticalDivider);
        binding.recyclerView.addItemDecoration(verticalDecoration);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        binding.recyclerView.addItemDecoration(horizontalDecoration);

        binding.recyclerView.setAdapter(adapter);

        loaddata();

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
    {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        private GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void loaddata() {
        String resounse=sessionManager.getResponse().get(SessionManager.KEY_RESPONSE);
        try {
            JSONObject jsonObject=new JSONObject(resounse);
            JSONObject basicInfoObj=jsonObject.getJSONObject("BasicChart");

            binding.nameTV.setText(jsonObject.getString("name"));
            binding.dobTV.setText(jsonObject.getString("DOB"));

            binding.basicNoTV.setText(basicInfoObj.getString("basicNo"));
            binding.destinyTV.setText(basicInfoObj.getString("dastinyNo"));
            binding.supportiveNotv.setText(basicInfoObj.getString("supportiveNo"));
            JSONArray basicChartArray=basicInfoObj.getJSONArray("basicChart");

            for (int i=0;i<basicChartArray.length();i++)
            {
                String basicchartString=basicChartArray.getString(i);
                BasicChartDTO basicChartDTO=new BasicChartDTO();
                basicChartDTO.setBasicchartvalue(basicchartString);
                basicChartDTOArrayList.add(basicChartDTO);

            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
