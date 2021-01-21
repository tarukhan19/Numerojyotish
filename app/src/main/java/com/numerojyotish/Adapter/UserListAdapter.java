package com.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.numerojyotish.DialogFragment.UpdateUserFragment;
import com.numerojyotish.Interface.UserListInterface;
import com.numerojyotish.Model.BasicChartDTO;
import com.numerojyotish.Model.UserListDTO;
import com.numerojyotish.R;
import com.numerojyotish.databinding.ItemChartBinding;
import com.numerojyotish.databinding.ItemViewcustomerBinding;
import com.numerojyotish.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolderPollAdapter>  implements Filterable {
    private Context mcontex;
    private List<UserListDTO> userListDTOList,userListDTOListFiltered;
    Activity activity;
    SessionManager sessionManager;
    ItemViewcustomerBinding binding;
    int pos;
    UserListInterface listener;


    static UserListAdapter adapter;
    public UserListAdapter(Context mcontex, List<UserListDTO> userListDTOList, UserListInterface listener)
    {
        this.mcontex = mcontex;
        this.userListDTOList = userListDTOList;
        this.userListDTOListFiltered=userListDTOList;
        activity= (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
        adapter=this;
        this.listener = listener;

    }



    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_viewcustomer, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.customerNameTV.setText(userListDTOList.get(position).getFirstname()+" "+userListDTOList.get(position).getLastname());
        holder.itemRowBinding.custMobileNoTV.setText(userListDTOList.get(position).getMobileno());
        holder.itemRowBinding.emailidTV.setText(userListDTOList.get(position).getEmailid());
        holder.itemRowBinding.expirationdateTV.setText(userListDTOList.get(position).getExpirydate());
        holder.itemRowBinding.dobTV.setText(userListDTOList.get(position).getDateofbirth());


        binding.editcustomerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                sessionManager.setCustomerDetails(userListDTOList.get(position).getFirstname(),userListDTOList.get(position).getLastname(),
                        userListDTOList.get(position).getMobileno(), userListDTOList.get(position).getEmailid(),userListDTOList.get(position).getDateofbirth(),
                        userListDTOList.get(position).getExpirydate(),userListDTOList.get(position).getGender() );
                UpdateUserFragment dialogFragment = new UpdateUserFragment();
                FragmentTransaction ft = ((AppCompatActivity)mcontex).getSupportFragmentManager().beginTransaction();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");
            }
        });

    }
    public static UserListAdapter getInstance()
    {
        return adapter;
    }

    public void runThread(final String firstName, final String lastName, final String gender, final String dateOfBirth, final String dateOfMembExpDate,
                          final String mobileNo, final String emailId)
    {
        new Thread() {
            public void run() {
                try {
                    ((Activity)mcontex).runOnUiThread(new Runnable()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {


                            userListDTOList.get(pos).setFirstname(firstName);
                            userListDTOList.get(pos).setLastname(lastName);
                            userListDTOList.get(pos).setGender(gender);
                            userListDTOList.get(pos).setDateofbirth(dateOfBirth);
                            userListDTOList.get(pos).setExpirydate(dateOfMembExpDate);
                            userListDTOList.get(pos).setMobileno(mobileNo);
                            userListDTOList.get(pos).setEmailid(emailId);

                            notifyDataSetChanged();

                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    public int getItemCount() {
        return userListDTOList != null ? userListDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemViewcustomerBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemViewcustomerBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    userListDTOList = userListDTOListFiltered;
                } else {
                    ArrayList<UserListDTO> filteredList = new ArrayList<>();
                    for (UserListDTO row : userListDTOListFiltered) {

                        if ((row.getFirstname()

                        ).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }

                    userListDTOList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = userListDTOList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userListDTOList = (ArrayList<UserListDTO>) filterResults.values;

                if (userListDTOList.isEmpty()) {
                    //norecordfoundTV.setVisibility(View.GONE);
                    notifyDataSetChanged();

                } else {
                    //norecordfoundTV.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }

            }
        };
    }


}