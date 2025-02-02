package com.survivalcoding.todolist.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.survivalcoding.todolist.App
import com.survivalcoding.todolist.R
import com.survivalcoding.todolist.databinding.FragmentMainBinding
import com.survivalcoding.todolist.presentation.MainViewModel
import com.survivalcoding.todolist.presentation.MainViewModelFactory
import com.survivalcoding.todolist.presentation.add.AddFragment
import com.survivalcoding.todolist.presentation.main.adapter.TodoListAdapter

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory((requireActivity().application as App).todoRepository)
    }
    private val adapter by lazy {
        TodoListAdapter({ todo -> viewModel.updateIsDone(todo) }, { todo ->
            viewModel.setTodo(todo)
            moveToAdd()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView 설정
        binding.mainRvTodo.adapter = adapter

        // 작성하기 화면으로 이동
        binding.mainFabAdd.setOnClickListener { moveToAdd() }

        // 할 일 검색
        binding.mainEtSearch.doAfterTextChanged { query ->
            // 검색어 지웠을 때 전체 리스트 조회
            if (query.isNullOrEmpty()) viewModel.getTodoList()
            // 검색어 입력시 자동 검색
            else viewModel.search(query.toString())
        }

        // todolist 업데이트 관찰
        viewModel.todoList.observe(this) { list ->
            adapter.submitList(list)
        }

        // 할일 목록 조회
        viewModel.getTodoList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun moveToAdd() {
        parentFragmentManager.commit {
            replace<AddFragment>(R.id.main_fragment_container_view)
            setReorderingAllowed(true)
            addToBackStack(null) // name can be null
        }
    }
}