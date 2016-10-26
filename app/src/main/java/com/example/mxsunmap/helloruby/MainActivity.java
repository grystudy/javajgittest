package com.example.mxsunmap.helloruby;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context mContext = getApplicationContext();
        cloneRepo();
        setContentView(R.layout.activity_main);
    }

    public boolean cloneRepo() {
        File localRepo = new File("/data/data/com.example.mxsunmap.helloruby/files/repo", "test12");
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI("https://github.com/grystudy/test.git").setCloneAllBranches(true)
//                .setURI("http://192.168.5.7/yangyu/LLDataConverter.git").setCloneAllBranches(true)
//                .setProgressMonitor(new RepoCloneMonitor())
//                .setTransportConfigCallback(new SgitTransportCallback())
                .setDirectory(localRepo)
                .setCloneSubmodules(false);

        String username = "yangy@meixing.com";
        username = "grystudy";
        String password ="gry654321";

        if (username != null && password != null && !username.equals("")
                && !password.equals("")) {
            UsernamePasswordCredentialsProvider auth = new UsernamePasswordCredentialsProvider(
                    username, password);
            cloneCommand.setCredentialsProvider(auth);
        }
        try {
         Git newgitdegub =  cloneCommand.call();
//            Profile.setLastCloneSuccess();
        }
//        catch (InvalidRemoteException e) {
//            setException(e, R.string.error_invalid_remote);
//            Profile.setLastCloneFailed(mRepo);
//            return false;
//        } catch (TransportException e) {
//            setException(e);
//            Profile.setLastCloneFailed(mRepo);
//            handleAuthError(mOnPasswordEnter);
//            return false;
//        } catch (GitAPIException e) {
//            setException(e, R.string.error_clone_failed);
//            return false;
//        } catch (JGitInternalException e) {
//            setException(e);
//            return false;
//        }
        catch (OutOfMemoryError e) {
//            setException(e, R.string.error_out_of_memory);
            return false;
        } catch (Throwable e) {
//            setException(e);
            return false;
        }
        return true;
    }
}
