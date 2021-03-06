package com.mauriciotogneri.androidutils.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mauriciotogneri.androidutils.FragmentParameters;
import com.mauriciotogneri.androidutils.permissions.Permissions;
import com.mauriciotogneri.androidutils.permissions.PermissionsResult;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class BaseFragment<V extends BaseView, P extends BaseParameters> extends Fragment implements DialogDisplayer
{
    protected V view;
    protected P parameters;
    protected BaseActivity<?, ?> activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        loadParameters();

        return setupView(inflater, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initialize();
    }

    private void loadParameters()
    {
        parameters = parameters();

        if (parameters != null)
        {
            parameters.bind(this);
        }
    }

    private View setupView(LayoutInflater inflater, ViewGroup container)
    {
        view = view();

        return view.inflate(inflater, container);
    }

    protected ScreenTransition screenTransition()
    {
        return new ScreenTransition();
    }

    protected abstract V view();

    protected P parameters()
    {
        return null;
    }

    protected void initialize()
    {
    }

    protected void replaceFragment(BaseFragment<?, ?> fragment)
    {
        activity.replaceFragment(fragment);
    }

    protected void addFragmentToStack(BaseFragment<?, ?> fragment)
    {
        activity.addFragmentToStack(fragment);
    }

    protected void finish()
    {
        activity.finish();
    }

    protected void back()
    {
        activity.popFragment();
    }

    protected void close()
    {
        activity.removeFragments();
    }

    protected Context context()
    {
        return activity;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(@NonNull Activity activity)
    {
        super.onAttach(activity);

        this.activity = (BaseActivity) activity;
    }

    protected Permissions permissions()
    {
        return new Permissions(this, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionsResult permissionsResult = new PermissionsResult(this);
        permissionsResult.process(requestCode, permissions, grantResults);
    }

    protected void post(Runnable runnable)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    protected void post(Runnable runnable, long delay)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delay);
    }

    protected <A> A parameter(String key, A defaultValue)
    {
        FragmentParameters parameters = new FragmentParameters(getArguments());

        return parameters.parameter(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    protected <A> A parent(Class<A> parentClass)
    {
        Fragment parentFragment = getParentFragment();

        if (parentClass.isInstance(parentFragment))
        {
            return (A) parentFragment;
        }
        else if (parentClass.isInstance(getActivity()))
        {
            return (A) getActivity();
        }

        return null;
    }

    protected DialogDisplayer dialogDisplayer()
    {
        return this;
    }

    @Override
    public String string(int resId, Object... args)
    {
        return isAdded() ? getString(resId, args) : "";
    }

    @Override
    public FragmentManager fragmentManager()
    {
        return getChildFragmentManager();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        // no call for super(), bug on API Level > 11
    }
}