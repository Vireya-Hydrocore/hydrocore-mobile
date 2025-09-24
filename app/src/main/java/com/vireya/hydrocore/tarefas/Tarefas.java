package com.vireya.hydrocore.tarefas;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vireya.hydrocore.R;
import com.vireya.hydrocore.RetrofitConfig;
import com.vireya.hydrocore.databinding.FragmentTarefasBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tarefas extends Fragment {

    private FragmentTarefasBinding binding;
    private final List<Tarefa> tarefas = new ArrayList<>();
    private TarefasAdapter tarefasAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTarefasBinding.inflate(inflater, container, false);

        ImageView btnVireya = binding.getRoot().findViewById(R.id.imgHydrocore);
        ImageView btnAgenda = binding.getRoot().findViewById(R.id.imgAgenda);
        ImageView btnConfig = binding.getRoot().findViewById(R.id.imgConfig);

        btnVireya.setOnClickListener(v -> {
            DrawerLayout drawer = requireActivity().findViewById(R.id.drawerLayout);
            if (drawer != null) drawer.openDrawer(GravityCompat.START);
        });

        btnAgenda.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_agenda, null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_tarefas, true)
                            .build()
            );
            DesmarcarTarefas();
        });

        btnConfig.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_configuracoes, null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_tarefas, true)
                            .build()
            );
            DesmarcarTarefas();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvTarefas.setLayoutManager(new LinearLayoutManager(requireContext()));
        tarefasAdapter = new TarefasAdapter(tarefas);
        binding.rvTarefas.setAdapter(tarefasAdapter);

        carregarTarefas("Ana Costa");
    }

    private void carregarTarefas(String nome) {
        TarefasApi api = RetrofitConfig.getTarefasApi();
        api.listarTarefasPorNome(nome).enqueue(new Callback<List<Tarefa>>() {
            @Override
            public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tarefas.clear();
                    tarefas.addAll(response.body());
                    tarefasAdapter.setTarefas(tarefas);
                }
            }

            @Override
            public void onFailure(Call<List<Tarefa>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void DesmarcarTarefas() {
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
        if (bottomNav != null) {
            bottomNav.getMenu().setGroupCheckable(0, true, false);
            bottomNav.getMenu().findItem(R.id.navigation_tarefas).setChecked(false);
            bottomNav.getMenu().setGroupCheckable(0, true, true);
        }
    }
}
