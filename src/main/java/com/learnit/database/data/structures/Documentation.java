package com.learnit.database.data.structures;

//Базоый класс, хранящий книги, статьи и прочий материал для работы
import com.learnit.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Documentation{
    private int docId;

    //Название документа
    private String docName;

    //Информация хранящаяся в доках в виде Html для дальнейшего отображения в HtmlEditor
    private String docText;

    //Информация о дате создания и последнего изменения файла
    private LocalDateTime docCreationDateTime;
    private LocalDateTime docLastEditDateTime;

    //Массив тегов для удобной сортировки и отображения информации
    private ArrayList<Tag> tags;

    //Информация по умолчанию
    public Documentation(){
        docCreationDateTime = docLastEditDateTime = LocalDateTime.now();
        String[] str = docCreationDateTime.toString().split("T");
        docId = -1;
        docName = "-1";
        docText = "-1";
    }


    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocText() {
        return docText;
    }

    public void setDocText(String docText) {
        this.docText = docText;
    }

    public LocalDateTime getDocCreationDateTime() {
        return docCreationDateTime;
    }

    public void setDocCreationDateTime(LocalDateTime docCreationDateTime) {
        this.docCreationDateTime = docCreationDateTime;
    }

    public LocalDateTime getDocLastEditDateTime() {
        return docLastEditDateTime;
    }

    public void setDocLastEditDateTime(LocalDateTime docLastEditDateTime) {
        this.docLastEditDateTime = docLastEditDateTime;
    }

}
