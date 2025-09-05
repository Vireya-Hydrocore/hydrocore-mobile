package com.vireya.hydrocore.estoque;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.estoque.adapter.ProdutoAdapter;
import com.vireya.hydrocore.estoque.api.ApiClient;
import com.vireya.hydrocore.estoque.api.ApiService;
import com.vireya.hydrocore.estoque.model.Produto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Estoque#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Estoque extends Fragment {
    private RecyclerView recyclerView;
    private ProdutoAdapter adapter;
    private List<Produto> productList = new ArrayList<>();
    private ApiService apiService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Estoque() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Estoque.
     */
    // TODO: Rename and change types and number of parameters
    public static Estoque newInstance(String param1, String param2) {
        Estoque fragment = new Estoque();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estoque, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProdutoAdapter(productList);
        recyclerView.setAdapter(adapter);

        //configurando o retrofit
        apiService = ApiClient.getClient().create(ApiService.class);

        //buscando os dados da api
        loadProdutos();

        //tabs
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Todos"));
        tabLayout.addTab(tabLayout.newTab().setText("Suficiente"));
        tabLayout.addTab(tabLayout.newTab().setText("Próx. ao fim"));
        tabLayout.addTab(tabLayout.newTab().setText("Insuficiente"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterList(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        return view;

    }

    private void loadProdutos(){
        apiService.getProdutos().enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful() && response.body() != null){
                    productList.addAll(response.body());
                    adapter.updateList(productList);
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar os produtos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                Toast.makeText(getContext(), "Falha: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void filterList(int position) {
        List<Produto> filteredList = new ArrayList<>();
        switch (position) {
            case 0:
                adapter.updateList(productList);
                return;
            case 1:
                for (Produto p : productList) if (p.getStatus().equals("Suficiente")) filteredList.add(p);
                break;
            case 2:
                for (Produto p : productList) if (p.getStatus().equals("Prox")) filteredList.add(p);
                break;
            case 3:
                for (Produto p : productList) if (p.getStatus().equals("Insuficiente")) filteredList.add(p);
                break;
        }
        adapter.updateList(filteredList);
    }
}