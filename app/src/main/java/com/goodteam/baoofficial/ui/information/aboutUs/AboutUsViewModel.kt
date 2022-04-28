package com.goodteam.baoofficial.ui.information.aboutUs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutUsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value =
            "Trong quá trình học tập, nhóm em nhận thấy có nhiều người không biết cách sử dụng những ứng dụng như Word, Excel,… hoặc những tính năng quen thuộc trong đó, hoặc có những người biết sử dụng những ứng dụng đó nhưng không biết nhiều mẹo và thủ thuật hay và bổ ích, làm cho cho các thao tác diễn ra chậm hơn, năng suất thấp hơn, hay cũng có nhiều người không thường xuyên theo dõi các tin tức về công nghệ, dẫn đến việc không thể cập nhật được các công nghệ mới. Vì thế nhóm nghiên cứu ra ứng dụng tổng hợp được những tin tức, hướng dẫn, những mẹo hay, thuận tiện hơn cho mọi người."
    }
    val text: LiveData<String> = _text
}