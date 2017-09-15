package com.tribe.explorer.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.controller.HomeManager;
import com.tribe.explorer.controller.LanguageManager;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Config;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.CategoriesData;
import com.tribe.explorer.model.beans.Language;
import com.tribe.explorer.model.beans.LanguageData;
import com.tribe.explorer.model.custom.ImagePicker;
import com.tribe.explorer.view.adapters.CategoriesAdapter;
import com.tribe.explorer.view.adapters.GalleryAdapter;
import com.tribe.explorer.view.adapters.HoursAdapter;
import com.tribe.explorer.view.adapters.LanguageAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;

public class AddListingFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    EditText editEmail, editBusinessName, editLocation, editContact, editRegion, editCategory,
            editBusinessLabel, editWeb, editPhone, editVideoURL, editDescription;

    TextView tvCover, tvGallery, tvAddHours, tvAddLanguage, tvDone;
    ImageView imgCover, imgDelete;
    RecyclerView recyclerHours, recyclerLanguage, recyclerGallery;
    HoursAdapter hoursAdapter;
    GalleryAdapter galleryAdapter;
    boolean isHours = false, isGallery = false;
    RelativeLayout rlCover;

    private static final int PERMISSION_REQUEST_CODE = 1001;
    static final int REQUEST_IMAGE_GALLERY = 1;
    static final int REQUEST_IMAGE_COVER = 2;
    private ArrayList<Uri> uriList;
    Uri uriCover;

    Dialog dialog, dialogCategories, dialogLanguage;
    TextView tvCancel, tvConfirm, tvLangCancel, tvLangAdd;
    EditText editOwner;
    String str_language, id;
    private List<CategoriesData.Data> categoriesList;
    private List<String> languagesList;
    List<LanguageData.Data> dataList;
    private List<Language> list;
    private LanguageAdapter languageAdapter;
    private ArrayAdapter<String> listAdapter;
    ImageView imgBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        initViews(view);

        return view;
    }

    public void initViews(View view) {
        languagesList = new ArrayList<>();
        dataList = new ArrayList<>();
        list = new ArrayList<>();
        dialog = Utils.showDialog(getActivity());
        dialog.show();
        ModelManager.getInstance().getLanguageManager().languageTask(Config.LANGUAGE_URL);

        uriList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        editEmail = view.findViewById(R.id.editEmail);
        editBusinessName = view.findViewById(R.id.editBusinessName);
        editLocation = view.findViewById(R.id.editLocation);
        editContact = view.findViewById(R.id.editContact);
        editRegion = view.findViewById(R.id.editRegion);
        editCategory = view.findViewById(R.id.editCategory);
        editBusinessLabel = view.findViewById(R.id.editBusinessLabel);
        editWeb = view.findViewById(R.id.editWeb);
        editPhone = view.findViewById(R.id.editPhone);
        editVideoURL = view.findViewById(R.id.editVideoURL);
        editDescription = view.findViewById(R.id.editDescription);

        tvCover = view.findViewById(R.id.tvCover);
        tvGallery = view.findViewById(R.id.tvGallery);
        tvAddHours = view.findViewById(R.id.tvAddHours);
        tvAddLanguage = view.findViewById(R.id.tvAddLanguage);
        tvDone = view.findViewById(R.id.tvDone);

        rlCover = view.findViewById(R.id.rlCover);
        imgCover = view.findViewById(R.id.imgCover);
        imgDelete = view.findViewById(R.id.imgDelete);
        imgBack = view.findViewById(R.id.imgBack);

        recyclerHours = view.findViewById(R.id.recyclerHours);
        recyclerHours.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerHours.setNestedScrollingEnabled(false);
        hoursAdapter = new HoursAdapter(getActivity());
        recyclerHours.setAdapter(hoursAdapter);

        recyclerLanguage = view.findViewById(R.id.recyclerLanguage);
        recyclerLanguage.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerLanguage.setNestedScrollingEnabled(false);

        recyclerGallery = view.findViewById(R.id.recyclerGallery);
        recyclerGallery.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        DividerItemDecoration decor = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL);
        decor.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerGallery.addItemDecoration(decor);
        recyclerGallery.setNestedScrollingEnabled(false);
        galleryAdapter = new GalleryAdapter(getActivity(), uriList);
        recyclerGallery.setAdapter(galleryAdapter);

        tvAddHours.setOnClickListener(this);
        tvAddLanguage.setOnClickListener(this);
        tvGallery.setOnClickListener(this);
        tvCover.setOnClickListener(this);
        editCategory.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        categoriesList.addAll(HomeManager.categoriesList);
        initDialog();
        initLanguageDialog();
    }

    public void initDialog() {
        dialogCategories = Utils.createDialog(getActivity(), R.layout.dialog_spinner_items);
        tvCancel = dialogCategories.findViewById(R.id.tvCancel);
        tvConfirm = dialogCategories.findViewById(R.id.tvConfirm);
        RecyclerView recyclerCategories = dialogCategories.findViewById(R.id.recyclerCategories);
        recyclerCategories.setHasFixedSize(true);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getActivity()));

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getActivity(), categoriesList);
        recyclerCategories.setAdapter(categoriesAdapter);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    public void initLanguageDialog() {
        dialogLanguage = Utils.createDialog(getActivity(), R.layout.dialog_language);
        Spinner spinner = dialogLanguage.findViewById(R.id.spinner);
        listAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, languagesList);

        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listAdapter);
        spinner.setOnItemSelectedListener(this);
        languageAdapter = new LanguageAdapter(getActivity(), list);
        recyclerLanguage.setAdapter(languageAdapter);

        editOwner = dialogLanguage.findViewById(R.id.editOwner);
        tvLangCancel = dialogLanguage.findViewById(R.id.tvLangCancel);
        tvLangAdd = dialogLanguage.findViewById(R.id.tvLangAdd);

        tvLangCancel.setOnClickListener(this);
        tvLangAdd.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        LanguageData.Data data = dataList.get(position);
        str_language = data.language;
        id = data.id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddHours:
                addHours();
                break;

            case R.id.tvAddLanguage:
                dialogLanguage.show();
                break;

            case R.id.tvGallery:
                isGallery = true;
                dispatchTakePictureIntent();
                break;

            case R.id.tvCover:
                isGallery = false;
                dispatchTakePictureIntent();
                break;

            case R.id.imgDelete:
                imgCover.setImageBitmap(null);
                rlCover.setVisibility(View.GONE);
                break;

            case R.id.editCategory:
                dialogCategories.show();
                break;

            case R.id.tvConfirm:
                setCategories();
                break;

            case R.id.tvCancel:
                dialogCategories.dismiss();
                break;

            case R.id.tvLangAdd:
                addLanguages();
                break;

            case R.id.tvLangCancel:
                dialogLanguage.dismiss();
                break;

            case R.id.imgBack:
                backScreen();
                break;
        }
    }

    public void addLanguages() {
        for (Language language : list) {
            if (id.equals(language.getId())) {
                Toast.makeText(getActivity(), "Language already added", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        dialogLanguage.dismiss();
        Language language = new Language(id, str_language, editOwner.getText().toString());
        list.add(language);
        languageAdapter.notifyDataSetChanged();
    }

    public void setCategories() {
        dialogCategories.dismiss();
        ArrayList<String> categories = new ArrayList<>();
        for (CategoriesData.Data data : categoriesList) {
            if (data.isSelected()) {
                categories.add(data.name);
            }
        }
        editCategory.setText(categories.toString().replace("[", "").replace("]", ""));
    }

    public void addHours() {
        if (!isHours) {
            recyclerHours.setVisibility(View.VISIBLE);
            isHours = true;
            tvAddHours.setText(R.string.minus);
        }
        else {
            recyclerHours.setVisibility(View.GONE);
            isHours = false;
            tvAddHours.setText(R.string.plus);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(Event event) {
        dialog.dismiss();
        EventBus.getDefault().removeAllStickyEvents();
        switch (event.getKey()) {
            case Constants.LANGUAGE_SUCCESS:
                dataList.addAll(LanguageManager.languagesList);
                for (LanguageData.Data data: dataList) {
                    languagesList.add(data.language);
                }
                listAdapter.notifyDataSetChanged();
                break;

            case Constants.LANGUAGE_EMPTY:
                break;

            case Constants.NO_RESPONSE:
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        if (Build.VERSION.SDK_INT >= M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);

        } else {
            chooseImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            chooseImage();
        } else {
            Toast.makeText(getActivity(), R.string.grant_permissions, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void chooseImage() {
        Intent chooseImageIntent;
        if (isGallery) {
            chooseImageIntent = ImagePicker.getMultiImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, REQUEST_IMAGE_GALLERY);
        } else {
            chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, REQUEST_IMAGE_COVER);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            recyclerGallery.setVisibility(View.VISIBLE);

            File imageFile = ImagePicker.getTempFile(getActivity());
            boolean isCamera = (data == null || data.getData() == null  ||
                    data.getData().toString().contains(imageFile.toString()));

            if (isCamera) {
                Bitmap photo = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                Uri tempUri = ImagePicker.getImageUri(getActivity(), photo);
                uriList.add(tempUri);
                galleryAdapter.notifyDataSetChanged();
                return;
            }

            for (int i=0; i<data.getClipData().getItemCount(); i++) {
                Uri selectedImage = data.getClipData().getItemAt(i).getUri();
                uriList.add(selectedImage);
                galleryAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_IMAGE_COVER && resultCode == RESULT_OK) {
            Bitmap photo = ImagePicker.getImageFromResult(getActivity(), resultCode, data);

            uriCover = ImagePicker.getImageUri(getActivity(), photo);
            //File finalFile = new File(ImagePicker.getRealPathFromURI(getActivity(), tempUri));
           // filePath = finalFile.getAbsolutePath();
            rlCover.setVisibility(View.VISIBLE);
            imgCover.setImageBitmap(photo);
        }
    }

    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

}