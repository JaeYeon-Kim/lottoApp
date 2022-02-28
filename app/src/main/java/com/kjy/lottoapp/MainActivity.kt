package com.kjy.lottoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {


    // 각 버튼 초기화
    // activity_main ui와 메인 액티비티 연결
    private val clearButton: Button by lazy {
        findViewById<Button>(R.id.clearButton)
    }

    private val addButton: Button by lazy {
        findViewById<Button>(R.id.addButton)
    }

    private val runButton: Button by lazy {
        findViewById<Button>(R.id.runButton)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.textView1),
            findViewById<TextView>(R.id.textView2),
            findViewById<TextView>(R.id.textView3),
            findViewById<TextView>(R.id.textView4),
            findViewById<TextView>(R.id.textView5),
            findViewById<TextView>(R.id.textView6)

        )
    }

    // 이미 자동생성으로 번호를 추가해서 번호추가하기로 추가할 수 없는 예외의 경우
    private var didRun = false


    // 중복된 숫자가 들어가더라도 더이상 추가되지 않게함.
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // NumberPicker의 숫자 최소, 최댓값을 설정하여 활성화
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()

    }

    // 자동생성 시작 버튼 함수
    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            // 다 함수에 인덱스값까지 전달
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number ,textView)

            }

        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {

            // 예외처리
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요. ", Toast.LENGTH_SHORT).show()

                // didRun 구문만 리턴
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            // 인덱스 시작 0
            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setNumberBackground(numberPicker.value, textView)


            pickNumberSet.add(numberPicker.value)

        }
    }

    // 색을 결정하는 코드의 중복 방지를 위해 설정
    private fun setNumberBackground(number:Int, textView: TextView) {

        when(number) {
            in 1..10 ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yello)
            in 11..20 ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }

    }

    // 초기화 버튼 구현
    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            // forEach 앞에서 부터 순차적으로 꺼내서 실행되는 함수
            numberTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }

    // 1~45까지의 랜덤 번호 생성
    // 6개의 리스트를 반환하는 함수가 됌.
    private fun getRandomNumber():List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for(i in 1..45) {

                    if (pickNumberSet.contains(i)) {
                        continue
                    }
                    this.add(i)
                }
            }
        numberList.shuffle()

        // 리스트에서 뽑아낸 숫자들에서 subList를 이용해 원하는 갯수만큼 추출

        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        // 랜덤하게 뽑아낸 숫자들을 정렬 시킴. (오름차순)
        return newList.sorted()
    }
}