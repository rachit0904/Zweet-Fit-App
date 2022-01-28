package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.practise.zweet_fit_app.Adapters.CoinTransactionParentAdapter;
import com.practise.zweet_fit_app.Modals.CoinTransactionChildModal;
import com.practise.zweet_fit_app.Modals.CoinTransactionParentModal;
import com.practise.zweet_fit_app.R;

import java.util.ArrayList;
import java.util.List;


public class CoinHistory extends Fragment {
    RecyclerView rv;
    List<CoinTransactionParentModal> coinTransactionParentModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_coin_history, container, false);
        rv=view.findViewById(R.id.coinParentRv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        CoinTransactionParentAdapter adapter=new CoinTransactionParentAdapter(getContext(),getTransactions());
        rv.setAdapter(adapter);
        return view;
    }

    private List<CoinTransactionParentModal> getTransactions() {
        CoinTransactionParentModal modal=new CoinTransactionParentModal();
        List<CoinTransactionChildModal> childModalList=new ArrayList<>();
        modal.setDate("1 Dec 2021");
        CoinTransactionChildModal childModal=new CoinTransactionChildModal();
        childModal.setCoins("+20");
        childModal.setSource("Daily Group Event");
        childModalList.add(childModal);
        CoinTransactionChildModal childModal2=new CoinTransactionChildModal();
        childModal2.setCoins("-5");
        childModal2.setSource("Peer Event Entry Fee");
        childModalList.add(childModal2);
        modal.setChildModalList(childModalList);
        coinTransactionParentModalList.add(modal);
        CoinTransactionParentModal modal2=new CoinTransactionParentModal();
        List<CoinTransactionChildModal> childModalList2=new ArrayList<>();
        modal2.setDate("3 Dec 2021");
        CoinTransactionChildModal childModal3=new CoinTransactionChildModal();
        childModal3.setCoins("-10");
        childModal3.setSource("Daily Group Event");
        childModalList2.add(childModal3);
        CoinTransactionChildModal childModal4=new CoinTransactionChildModal();
        childModal4.setCoins("+15");
        childModal4.setSource("Peer Event Prize");
        childModalList2.add(childModal4);
        CoinTransactionChildModal childModal5=new CoinTransactionChildModal();
        childModal5.setCoins("+15");
        childModal5.setSource("User Referal");
        childModalList2.add(childModal5);
        modal2.setChildModalList(childModalList2);
        coinTransactionParentModalList.add(modal2);
        return coinTransactionParentModalList;

    }
}