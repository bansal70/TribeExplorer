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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.tribe.explorer.controller.ListingManager;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Config;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.CategoriesData;
import com.tribe.explorer.model.beans.Language;
import com.tribe.explorer.model.beans.LanguageData;
import com.tribe.explorer.model.beans.ListingData;
import com.tribe.explorer.model.beans.RegionData;
import com.tribe.explorer.model.custom.ImagePicker;
import com.tribe.explorer.view.LocationActivity;
import com.tribe.explorer.view.adapters.CategoriesAdapter;
import com.tribe.explorer.view.adapters.EditHoursAdapter;
import com.tribe.explorer.view.adapters.GalleryAdapter;
import com.tribe.explorer.view.adapters.LanguageAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.defaultValue;
import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;

public class EditBusinessFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {
    private final String TAG = EditBusinessFragment.class.getSimpleName();

    EditText editEmail, editBusinessName, editContact, editCategory, editBusinessLabel,
            editWeb, editPhone, editVideoURL, editDescription,
            editFacebookUrl, editTwitterUrl, editInstagramUrl;
    Spinner spinnerRegion, spinnerTimezone;
    TextView editLocation, tvCompanyLogo, tvCover, tvGallery, tvAddHours, tvAddLanguage, tvDone;
    ImageView imgCover, imgDelete, imgLogo, imgLogoDelete;
    RecyclerView recyclerHours, recyclerLanguage, recyclerGallery;
    EditHoursAdapter editHoursAdapter;
    GalleryAdapter galleryAdapter;
    boolean isHours = false, isGallery = false, isCover = false;
    RelativeLayout rlCover, rlCompany;

    private static final int PERMISSION_REQUEST_CODE = 1001;
    static final int REQUEST_IMAGE_GALLERY = 1;
    static final int REQUEST_IMAGE_COVER = 2;
    static final int REQUEST_IMAGE_LOGO = 3;
    private ArrayList<Uri> uriList;
    Uri uriCover, uriLogo;

    Dialog dialog, dialogCategories, dialogLanguage;
    TextView tvCancel, tvConfirm, tvLangCancel, tvLangAdd;
    EditText editOwner;
    String str_language = "", id = "", timezone = "", region = "", email = "", user_id, lang;
    private List<CategoriesData.Data> categoriesList;
    private List<String> languagesList, timezoneList, regionsList, categories;
    List<LanguageData.Data> dataList;
    private List<Language> list;
    private LanguageAdapter languageAdapter;
    private ArrayAdapter<String> listAdapter, regionAdapter, timezoneAdapter;
    ImageView imgBack;
    private List<ListingData.Data> listingData;
    int position, listing_id;
    private ArrayList<Integer> catIdList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            listing_id = bundle.getInt("cat_id", defaultValue);
            position = bundle.getInt("position", defaultValue);
            Utils.location = "";
            email = TEPreferences.readString(getActivity(), "email");
            user_id = TEPreferences.readString(getActivity(), "user_id");
            lang = TEPreferences.readString(getActivity(), "lang");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        initViews(view);

        return view;
    }

    public void initViews(View view) {
        listingData = ListingManager.dataList;
        ListingData.Data data = listingData.get(position);
        Operations.jsonLanguages = new JSONArray();
        categories = new ArrayList<>();
        languagesList = new ArrayList<>();
        regionsList = new ArrayList<>();
        catIdList = new ArrayList<>();
        regionsList.add(getString(R.string.select_region));
        timezoneList = new ArrayList<>();
        timezoneList.add(getString(R.string.select_timezone));
        dataList = new ArrayList<>();
        list = new ArrayList<>();
        uriList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        dialog = Utils.showDialog(getActivity());
        dialog.show();
        ModelManager.getInstance().getLanguageManager().languageTask(getActivity(), Config.LANGUAGE_URL);

        editEmail = view.findViewById(R.id.editEmail);
        editBusinessName = view.findViewById(R.id.editBusinessName);
        editLocation = view.findViewById(R.id.editLocation);
        editContact = view.findViewById(R.id.editContact);
        editCategory = view.findViewById(R.id.editCategory);
        editBusinessLabel = view.findViewById(R.id.editBusinessLabel);
        editWeb = view.findViewById(R.id.editWeb);
        editPhone = view.findViewById(R.id.editPhone);
        editVideoURL = view.findViewById(R.id.editVideoURL);
        editDescription = view.findViewById(R.id.editDescription);
        editTwitterUrl = view.findViewById(R.id.editTwitterUrl);
        editFacebookUrl = view.findViewById(R.id.editFacebookUrl);
        editInstagramUrl = view.findViewById(R.id.editInstagramUrl);
        spinnerRegion = view.findViewById(R.id.spinnerRegion);
        spinnerTimezone = view.findViewById(R.id.spinnerTimezone);

        tvCover = view.findViewById(R.id.tvCover);
        tvCompanyLogo = view.findViewById(R.id.tvCompanyLogo);
        tvGallery = view.findViewById(R.id.tvGallery);
        tvAddHours = view.findViewById(R.id.tvAddHours);
        tvAddLanguage = view.findViewById(R.id.tvAddLanguage);
        tvDone = view.findViewById(R.id.tvDone);

        rlCover = view.findViewById(R.id.rlCover);
        rlCompany = view.findViewById(R.id.rlCompany);
        imgCover = view.findViewById(R.id.imgCover);
        imgDelete = view.findViewById(R.id.imgDelete);
        imgLogo = view.findViewById(R.id.imgLogo);
        imgLogoDelete = view.findViewById(R.id.imgLogoDelete);
        imgBack = view.findViewById(R.id.imgBack);

        recyclerHours = view.findViewById(R.id.recyclerHours);
        recyclerHours.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerHours.setNestedScrollingEnabled(false);

        editHoursAdapter = new EditHoursAdapter(getActivity(), data);
        recyclerHours.setAdapter(editHoursAdapter);
        recyclerHours.setVisibility(View.VISIBLE);

        recyclerLanguage = view.findViewById(R.id.recyclerLanguage);
        recyclerLanguage.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerLanguage.setNestedScrollingEnabled(false);

        recyclerGallery = view.findViewById(R.id.recyclerGallery);
        recyclerGallery.setVisibility(View.VISIBLE);
        recyclerGallery.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        DividerItemDecoration decor = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL);
        decor.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerGallery.addItemDecoration(decor);
        recyclerGallery.setNestedScrollingEnabled(false);
        galleryAdapter = new GalleryAdapter(getActivity(), uriList);
        recyclerGallery.setAdapter(galleryAdapter);

        editLocation.setOnClickListener(this);
        tvAddHours.setOnClickListener(this);
        tvAddLanguage.setOnClickListener(this);
        tvGallery.setOnClickListener(this);
        tvCover.setOnClickListener(this);
        tvCompanyLogo.setOnClickListener(this);
        editCategory.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        imgLogoDelete.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        spinnerRegion.setOnItemSelectedListener(this);
        spinnerTimezone.setOnItemSelectedListener(this);
        tvDone.setOnClickListener(this);

        categoriesList.addAll(HomeManager.categoriesList);
        initDialog();
        initLanguageDialog();
        setRegions();
    }

    public void setRegions() {
        editEmail.setText(email);
        regionAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, regionsList);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);

        timezoneAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, timezoneList);
        timezoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimezone.setAdapter(timezoneAdapter);
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

    public void setData() {
        editBusinessName.setText(listingData.get(position).title);
        editLocation.setText(listingData.get(position).loc);
        editContact.setText(listingData.get(position).contactEmail);
        int regionPosition = regionAdapter.getPosition(listingData.get(position).region.get(0));
        spinnerRegion.setSelection(regionPosition);
        for (int id : listingData.get(position).category_id) {
            catIdList.add(id);
        }
        editCategory.setText(listingData.get(position).category.toString().replace("[", "").replace("]", ""));
        editBusinessLabel.setText(listingData.get(position).label.toString().replace("[", "").replace("]", ""));
        editWeb.setText(listingData.get(position).website);
        editPhone.setText(listingData.get(position).phone);
        editVideoURL.setText(listingData.get(position).vedio);
        editDescription.setText(listingData.get(position).content);
        editFacebookUrl.setText(listingData.get(position).facebookUrl);
        editTwitterUrl.setText(listingData.get(position).twitterUrl);
        editInstagramUrl.setText(listingData.get(position).instagramUrl);
        int timezonePosition = timezoneAdapter.getPosition(listingData.get(position).jobHoursTimezone);
        spinnerTimezone.setSelection(timezonePosition);
        Utils.setImage(getActivity(), listingData.get(position).coverImage, imgCover);

        if (!listingData.get(position).companyAvatar.isEmpty())
            rlCompany.setVisibility(View.VISIBLE);
        if (!listingData.get(position).coverImage.isEmpty())
            rlCover.setVisibility(View.VISIBLE);
        String[] split = listingData.get(position).companyAvatar.split("/");
        String name = split[split.length-1];

        Utils.downloadImage(getActivity(), listingData.get(position).companyAvatar, name, imgLogo, "logo");
        Utils.downloadImage(getActivity(), listingData.get(position).coverImage, name, imgCover, "cover");
       // Utils.setImage(getActivity(), listingData.get(position).companyAvatar, imgLogo);
        //Utils.setImage(getActivity(), listingData.get(position).coverImage, imgCover);

        try {
            URL urlCover = new URL(listingData.get(position).coverImage);
            uriCover = Uri.parse(urlCover.toURI().toString());
            URL urlLogo = new URL(listingData.get(position).companyAvatar);
            uriLogo = Uri.parse(urlLogo.toURI().toString());
         //   imgCover.setImageURI(uriCover);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String images : listingData.get(position).galleryImages) {
            try {
                Utils.downloadImage(getActivity(), images, name, null, "gallery");
                URL url = new URL(images);
                Uri uri = Uri.parse(url.toURI().toString());
                uriList.add(uri);
                galleryAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i=0; i < listingData.get(position).language.size(); i++) {
            String owner = listingData.get(position).language.get(i).name;
            String lang = listingData.get(position).language.get(i).language;

            Language language = new Language(owner, lang, owner);
            list.add(language);
        }
        languageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editLocation:
                getActivity().startActivity(new Intent(getActivity(), LocationActivity.class));
                break;

            case R.id.tvAddHours:
                addHours();
                break;

            case R.id.tvAddLanguage:
                dialogLanguage.show();
                break;

            case R.id.tvGallery:
                isGallery = true;
                isCover = false;
                dispatchTakePictureIntent();
                break;

            case R.id.tvCover:
                isGallery = false;
                isCover = true;
                dispatchTakePictureIntent();
                break;

            case R.id.imgDelete:
                uriCover = null;
                imgCover.setImageBitmap(null);
                rlCover.setVisibility(View.GONE);
                break;

            case R.id.tvCompanyLogo:
                isGallery = false;
                isCover = false;
                dispatchTakePictureIntent();
                break;

            case R.id.imgLogoDelete:
                uriLogo = null;
                imgLogo.setImageBitmap(null);
                rlCompany.setVisibility(View.GONE);
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

            case R.id.tvDone:
                dialog.show();
                Operations.jsonLanguages = new JSONArray();
                Operations.addLanguages(list);
                String business = editBusinessName.getText().toString();
                String location = editLocation.getText().toString();
                String phone = editPhone.getText().toString();
                String contact_email = editContact.getText().toString();
                String labels = editBusinessLabel.getText().toString();
                String website = editWeb.getText().toString();
                String videoUrl = editVideoURL.getText().toString();
                String description = editDescription.getText().toString();
                String businessCategories = catIdList.toString().replace("[", "").replace("]", "");
                String languages = Operations.jsonLanguages.toString();
                String facebookUrl  = editFacebookUrl.getText().toString();
                String twitterUrl  = editTwitterUrl.getText().toString();
                String instagramUrl  = editInstagramUrl.getText().toString();
                String hours = Operations.hoursOfOperation;
                Log.e(TAG, "hours: "+hours);

                /*if (!hours.isEmpty()) {
                    hours = hours.substring(1, hours.length()-1);
                }*/

                String params = Operations.getEditListingParams(listing_id, user_id, email, business,
                        contact_email, description, location, website, videoUrl, phone, businessCategories,
                        labels, region, hours, languages, timezone, Utils.uriGallery.size(), facebookUrl,
                        twitterUrl, instagramUrl, lang);
                ModelManager.getInstance().getAddListingManager().addListingTask(getActivity(),
                        params, Utils.uriLogo, Utils.uriCover, Utils.uriGallery);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner:
                LanguageData.Data data = dataList.get(position);
                str_language = data.language;
                id = data.id;
                break;

            case R.id.spinnerRegion:
                if (position > 0)
                    region = regionsList.get(position);
                else
                    region = "";
                break;

            case R.id.spinnerTimezone:
                if (position > 0)
                    timezone = timezoneList.get(position);
                else
                    timezone = "";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onStart() {
        super.onStart();
        if (!Utils.location.isEmpty())
            editLocation.setText(Utils.location);
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
            case Constants.DETAILS_SUCCESS:
                dataList.addAll(LanguageManager.languagesList);
                for (LanguageData.Data data: dataList) {
                    languagesList.add(data.language);
                }
                listAdapter.notifyDataSetChanged();
                for (RegionData.Data data : LanguageManager.regionsList) {
                    regionsList.add(data.name);
                }
                regionAdapter.notifyDataSetChanged();
                timezoneList.addAll(LanguageManager.timezoneList);
                timezoneAdapter.notifyDataSetChanged();
                setData();
                break;

            case Constants.LANGUAGE_EMPTY:
                backScreen();
                Toast.makeText(getActivity(), R.string.error_processing_request, Toast.LENGTH_SHORT).show();
                break;

            case Constants.REGION_EMPTY:
                Toast.makeText(getActivity(), R.string.error_processing_request, Toast.LENGTH_SHORT).show();
                backScreen();
                break;

            case Constants.TIMEZONE_EMPTY:
                Toast.makeText(getActivity(), R.string.error_processing_request, Toast.LENGTH_SHORT).show();
                backScreen();
                break;

            case Constants.ADD_LISTING_SUCCESS:
                backScreen();
                Toast.makeText(getActivity(), R.string.listing_updated, Toast.LENGTH_SHORT).show();
                break;

            case Constants.ADD_LISTING_FAILED:
                Toast.makeText(getActivity(), R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                break;
        }
    }

    public void addLanguages() {
        if (editOwner.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
            return;
        }
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
        categories = new ArrayList<>();
        catIdList = new ArrayList<>();
        for (CategoriesData.Data data : categoriesList) {
            if (data.isSelected()) {
                categories.add(data.name);
                catIdList.add(data.termId);
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
            chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, REQUEST_IMAGE_GALLERY);
        } else if (isCover){
            chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, REQUEST_IMAGE_COVER);
        } else {
            chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, REQUEST_IMAGE_LOGO);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            recyclerGallery.setVisibility(View.VISIBLE);
            Bitmap photo = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
            Uri uriGallery = ImagePicker.getImageUri(getActivity(), photo);
            uriList.add(uriGallery);
            galleryAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_IMAGE_COVER && resultCode == RESULT_OK) {
            Bitmap photo = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
            uriCover = ImagePicker.getImageUri(getActivity(), photo);
            rlCover.setVisibility(View.VISIBLE);
            imgCover.setImageBitmap(photo);
        } else if (requestCode == REQUEST_IMAGE_LOGO && resultCode == RESULT_OK) {
            Bitmap photo = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
            uriLogo = ImagePicker.getImageUri(getActivity(), photo);
            rlCompany.setVisibility(View.VISIBLE);
            imgLogo.setImageBitmap(photo);
        }
    }


    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for (CategoriesData.Data data : categoriesList) {
            data.setSelected(false);
        }
    }
}
