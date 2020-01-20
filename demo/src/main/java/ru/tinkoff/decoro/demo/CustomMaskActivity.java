/*
 * Copyright © 2016 Tinkoff Bank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.tinkoff.decoro.demo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.TextUtils;
import ru.tinkoff.decoro.parser.SlotsParser;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

/**
 * @author Mikhail Artemev
 */

public class CustomMaskActivity extends AppCompatActivity {

    private EditText dataEdit;

    private MaskFormatWatcher formatWatcher;
    private SlotsParser slotsParser = new UnderscoreDigitSlotsParser();

    private TextWatcher maskTextWatcher = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            final MaskImpl maskDescriptor;

            if (TextUtils.isEmpty(s)) {
                maskDescriptor = createEmptyMask();
            } else {
                maskDescriptor = MaskImpl.createTerminated(slotsParser.parseSlots(s.toString()));
                maskDescriptor.setHideHardcodedHead(false);
                maskDescriptor.insertFront(dataEdit.getText().toString());
            }

            formatWatcher.setMask(maskDescriptor);
        }
    };

    @NonNull
    private MaskImpl createEmptyMask() {
        return MaskImpl.createNonTerminated(new Slot[]{PredefinedSlots.any()});
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_mask);

        EditText maskEdit = (EditText) findViewById(R.id.editMask);
        maskEdit.addTextChangedListener(maskTextWatcher);

        formatWatcher = new MaskFormatWatcher(createEmptyMask());

        dataEdit = (EditText) findViewById(R.id.editData);
        formatWatcher.installOn(dataEdit);
    }


}
