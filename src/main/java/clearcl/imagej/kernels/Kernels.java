package clearcl.imagej.kernels;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import java.util.HashMap;


/**
 * This class contains convenience access functions for OpenCL based
 * image processing.
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class Kernels
{
  public static boolean absolute(ClearCLIJ pCLIJ,
                                  ClearCLImage src,
                                  ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
              "Error: number of dimensions don't match! (addScalar)");
      return false;
    }

    return pCLIJ.execute(Kernels.class,
            "math.cl",
            "absolute_" + src.getDimension() + "d",
            lParameters);
  }


  public static boolean addPixelwise(ClearCLIJ pCLIJ,
                                     ClearCLImage src,
                                     ClearCLImage src1,
                                     ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("src1", src1);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(),
                         src1.getDimension(),
                         dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (addPixelwise)");
      return false;
    }
    return pCLIJ.execute(Kernels.class,
                         "math.cl",
                         "addPixelwise_" + src.getDimension() + "d",
                         lParameters);
  }

  public static boolean addScalar(ClearCLIJ pCLIJ,
                                  ClearCLImage src,
                                  ClearCLImage dst,
                                  float scalar)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("scalar", scalar);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (addScalar)");
      return false;
    }

    return pCLIJ.execute(Kernels.class,
                         "math.cl",
                         "addScalar_" + src.getDimension() + "d",
                         lParameters);
  }

  public static boolean addWeightedPixelwise(ClearCLIJ pCLIJ,
                                             ClearCLImage src,
                                             ClearCLImage src1,
                                             ClearCLImage dst,
                                             float factor,
                                             float factor1)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("src1", src1);
    lParameters.put("factor", factor);
    lParameters.put("factor1", factor1);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(),
                         src1.getDimension(),
                         dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (addScalar)");
      return false;
    }

    return pCLIJ.execute(Kernels.class,
                         "math.cl",
                         "addWeightedPixelwise_"
                         + src.getDimension()
                         + "d",
                         lParameters);
  }

  public static boolean argMaxProjection(ClearCLIJ pCLIJ,
                                         ClearCLImage src,
                                         ClearCLImage dst_max,
                                         ClearCLImage dst_arg)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("dst_max", dst_max);
    lParameters.put("dst_arg", dst_arg);

    pCLIJ.execute(Kernels.class,
                  "projections.cl",
                  "arg_max_project_3d_2d",
                  lParameters);

    return true;
  }

  public static boolean binaryAnd(ClearCLIJ pCLIJ,
                                         ClearCLImage src1,
                                         ClearCLImage src2,
                                         ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src1", src1);
    lParameters.put("src2", src2);
    lParameters.put("dst", dst);

    pCLIJ.execute(Kernels.class,
            "binaryProcessing.cl",
            "binary_and_" + src1.getDimension() + "d",
            lParameters);

    return true;
  }

  public static boolean binaryNot(ClearCLIJ pCLIJ,
                                  ClearCLImage src1,
                                  ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src1", src1);
    lParameters.put("dst", dst);

    pCLIJ.execute(Kernels.class,
            "binaryProcessing.cl",
            "binary_not_" + src1.getDimension() + "d",
            lParameters);

    return true;
  }

  public static boolean binaryOr(ClearCLIJ pCLIJ,
                                  ClearCLImage src1,
                                  ClearCLImage src2,
                                  ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src1", src1);
    lParameters.put("src2", src2);
    lParameters.put("dst", dst);

    pCLIJ.execute(Kernels.class,
            "binaryProcessing.cl",
            "binary_or_" + src1.getDimension() + "d",
            lParameters);

    return true;
  }



  public static boolean blur(ClearCLIJ pCLIJ,
                             ClearCLImage src,
                             ClearCLImage dst,
                             int nX,
                             int nY,
                             float sigmaX,
                             float sigmaY)
  {

    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("Nx", nX);
    lParameters.put("Ny", nY);
    lParameters.put("sx", sigmaX);
    lParameters.put("sy", sigmaY);
    lParameters.put("src", src);
    lParameters.put("dst", dst);
    return pCLIJ.execute(Kernels.class,
                         "blur.cl",
                         "gaussian_blur_image2d",
                         lParameters);
  }

  public static boolean blur(ClearCLIJ pCLIJ,
                             ClearCLImage src,
                             ClearCLImage dst,
                             int nX,
                             int nY,
                             int nZ,
                             float sigmaX,
                             float sigmaY,
                             float sigmaZ)
  {

    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("Nx", nX);
    lParameters.put("Ny", nY);
    lParameters.put("Nz", nZ);
    lParameters.put("sx", sigmaX);
    lParameters.put("sy", sigmaY);
    lParameters.put("sz", sigmaZ);
    lParameters.put("src", src);
    lParameters.put("dst", dst);
    return pCLIJ.execute(Kernels.class,
                         "blur.cl",
                         "gaussian_blur_image3d",
                         lParameters);
  }

  public static boolean blurSeparable(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, float... blurSigma) {
    int[] n = new int[blurSigma.length];

    for (int d = 0; d < n.length; d++) {
      n[d] = Math.max(1, (int) Math.round(2 * 3.5 * blurSigma[d]));
      if (n[d] % 2 != 1) {
        n[d] = n[d] + 1;
      }
      System.out.println("n[" + d + "] = " + n[d]);
    }


    ClearCLImage temp = clij.createCLImage(dst);

    HashMap<String, Object> lParameters = new HashMap<>();

    lParameters.clear();
    lParameters.put("N", n[0]);
    lParameters.put("s", blurSigma[0]);
    lParameters.put("dim", 0);
    lParameters.put("src", src);
    lParameters.put("dst", dst);
    clij.execute(Kernels.class,
            "blur.cl",
            "gaussian_blur_sep_image3d",
            lParameters);

    lParameters.clear();
    lParameters.put("N", n[1]);
    lParameters.put("s", blurSigma[1]);
    lParameters.put("dim", 1);
    lParameters.put("src", dst);
    lParameters.put("dst", temp);
    clij.execute(Kernels.class,
            "blur.cl",
            "gaussian_blur_sep_image3d",
            lParameters);

    lParameters.clear();
    lParameters.put("N", n[2]);
    lParameters.put("s", blurSigma[2]);
    lParameters.put("dim", 2);
    lParameters.put("src", temp);
    lParameters.put("dst", dst);
    clij.execute(Kernels.class,
            "blur.cl",
            "gaussian_blur_sep_image3d",
            lParameters);

    temp.close();
    return true;
  }


  public static boolean blurSlicewise(ClearCLIJ pCLIJ,
                                      ClearCLImage src,
                                      ClearCLImage dst,
                                      int nX,
                                      int nY,
                                      float sigmaX,
                                      float sigmaY)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("Nx", nX);
    lParameters.put("Ny", nY);
    lParameters.put("sx", sigmaX);
    lParameters.put("sy", sigmaY);
    lParameters.put("src", src);
    lParameters.put("dst", dst);
    return pCLIJ.execute(Kernels.class,
                         "blur.cl",
                         "gaussian_blur_slicewise_image3d",
                         lParameters);
  }

  @Deprecated public static boolean convert(ClearCLIJ clij,
                                            ClearCLBuffer src,
                                            ClearCLImage dst)
  {
    return copy(clij, src, dst);
  }

  @Deprecated public static boolean convert(ClearCLIJ clij,
                                            ClearCLImage src,
                                            ClearCLBuffer dst)
  {
    return copy(clij, src, dst);
  }

  public static boolean copy(ClearCLIJ clij,
                             ClearCLImage src,
                             ClearCLBuffer dst)
  {
    return copyInternal(clij,
                        src,
                        dst,
                        src.getDimension(),
                        dst.getDimension());
  }

  private static boolean copyInternal(ClearCLIJ clij,
                                      Object src,
                                      Object dst,
                                      long srcNumberOfDimensions,
                                      long dstNumberOfDimensions)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("dst", dst);
    if (!checkDimensions(srcNumberOfDimensions,
                         dstNumberOfDimensions))
    {
      System.out.println(
          "Error: number of dimensions don't match! (copy)");
      return false;
    }
    return clij.execute(Kernels.class,
                        "duplication.cl",
                        "copy_" + srcNumberOfDimensions + "d",
                        lParameters);
  }

  public static boolean copy(ClearCLIJ clij,
                             ClearCLBuffer src,
                             ClearCLImage dst)
  {
    return copyInternal(clij,
                        src,
                        dst,
                        src.getDimension(),
                        dst.getDimension());
  }

  public static boolean copy(ClearCLIJ clij,
                             ClearCLImage src,
                             ClearCLImage dst)
  {
    return copyInternal(clij,
                        src,
                        dst,
                        src.getDimension(),
                        dst.getDimension());
  }

  public static boolean copy(ClearCLIJ clij,
                             ClearCLBuffer src,
                             ClearCLBuffer dst)
  {
    return copyInternal(clij,
                        src,
                        dst,
                        src.getDimension(),
                        dst.getDimension());
  }

  public static boolean copySlice(ClearCLIJ clij,
                                  ClearCLImage src,
                                  ClearCLImage dst,
                                  int planeIndex)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("slice", planeIndex);
    return clij.execute(Kernels.class,
                        "duplication.cl",
                        "copySlice",
                        parameters);

  }

  public static boolean copySlice(ClearCLIJ clij,
                                  ClearCLBuffer src,
                                  ClearCLBuffer dst,
                                  int planeIndex)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("slice", planeIndex);
    return clij.execute(Kernels.class,
                        "duplication.cl",
                        "copySlice",
                        parameters);

  }

  public static boolean crop(ClearCLIJ clij,
                             ClearCLImage src,
                             ClearCLImage dst,
                             int startX,
                             int startY,
                             int startZ)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("start_x", startX);
    parameters.put("start_y", startY);
    parameters.put("start_z", startZ);
    return clij.execute(Kernels.class,
                        "duplication.cl",
                        "crop_3d",
                        parameters);
  }

  public static boolean crop(ClearCLIJ clij,
                             ClearCLImage src,
                             ClearCLImage dst,
                             int startX,
                             int startY)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("start_x", startX);
    parameters.put("start_y", startY);
    return clij.execute(Kernels.class,
                        "duplication.cl",
                        "crop_2d",
                        parameters);
  }

  public static boolean crop(ClearCLIJ clij,
                             ClearCLBuffer src,
                             ClearCLBuffer dst,
                             int startX,
                             int startY,
                             int startZ)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("start_x", startX);
    parameters.put("start_y", startY);
    parameters.put("start_z", startZ);
    return clij.execute(Kernels.class,
                        "duplication.cl",
                        "crop_3d",
                        parameters);
  }

  public static boolean crop(ClearCLIJ clij,
                             ClearCLBuffer src,
                             ClearCLBuffer dst,
                             int startX,
                             int startY)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("start_x", startX);
    parameters.put("start_y", startY);
    return clij.execute(Kernels.class,
                        "duplication.cl",
                        "crop_2d",
                        parameters);
  }

  public static boolean detectMaxima(ClearCLIJ clij,
                                     ClearCLImage src,
                                     ClearCLImage dst,
                                     int radius)
  {
    return detectOptima(clij, src, dst, radius, true);
  }

  public static boolean detectMinima(ClearCLIJ clij,
                                     ClearCLImage src,
                                     ClearCLImage dst,
                                     int radius)
  {
    return detectOptima(clij, src, dst, radius, false);
  }

  public static boolean detectOptima(ClearCLIJ clij,
                                     ClearCLImage src,
                                     ClearCLImage dst,
                                     int radius,
                                     boolean detectMaxima)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("radius", radius);
    parameters.put("detect_maxima", detectMaxima ? 1 : 0);
    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (detectOptima)");
      return false;
    }
    return clij.execute(Kernels.class,
                        "detection.cl",
                        "detect_local_optima_"
                        + src.getDimension()
                        + "d",
                        parameters);
  }

  public static boolean differenceOfGaussian(ClearCLIJ clij,
                                             ClearCLImage src,
                                             ClearCLImage dst,
                                             int radius,
                                             float sigmaMinuend,
                                             float sigmaSubtrahend)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("radius", radius);
    parameters.put("sigma_minuend", sigmaMinuend);
    parameters.put("sigma_subtrahend", sigmaSubtrahend);
    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (copy)");
      return false;
    }
    return clij.execute(Kernels.class,
                        "differenceOfGaussian.cl",
                        "subtract_convolved_images_"
                        + src.getDimension()
                        + "d_fast",
                        parameters);
  }

  public static boolean dilate(ClearCLIJ clij,
                               ClearCLImage src,
                               ClearCLImage dst)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (copy)");
      return false;
    }
    return clij.execute(Kernels.class,
                        "binaryProcessing.cl",
                        "dilate_diamond_neighborhood_"
                        + src.getDimension()
                        + "d",
                        parameters);
  }

  public static boolean downsample(ClearCLIJ clij,
                                   ClearCLImage src,
                                   ClearCLImage dst,
                                   float factorX,
                                   float factorY,
                                   float factorZ)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("factor_x", 1.f / factorX);
    parameters.put("factor_y", 1.f / factorY);
    parameters.put("factor_z", 1.f / factorZ);
    return clij.execute(Kernels.class,
                        "downsampling.cl",
                        "downsample_3d_nearest",
                        parameters);
  }

  public static boolean downsample(ClearCLIJ clij,
                                   ClearCLImage src,
                                   ClearCLImage dst,
                                   float factorX,
                                   float factorY)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("factor_x", 1.f / factorX);
    parameters.put("factor_y", 1.f / factorY);
    return clij.execute(Kernels.class,
                        "downsampling.cl",
                        "downsample_2d_nearest",
                        parameters);
  }

  public static boolean erode(ClearCLIJ clij,
                              ClearCLImage src,
                              ClearCLImage dst)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (copy)");
      return false;
    }

    return clij.execute(Kernels.class,
                        "binaryProcessing.cl",
                        "erode_diamond_neighborhood_"
                        + src.getDimension()
                        + "d",
                        parameters);
  }

  public static boolean flip(ClearCLIJ clij,
                             ClearCLImage src,
                             ClearCLImage dst,
                             boolean flipx,
                             boolean flipy,
                             boolean flipz)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("flipx", flipx ? 1 : 0);
    parameters.put("flipy", flipy ? 1 : 0);
    parameters.put("flipz", flipz ? 1 : 0);
    return clij.execute(Kernels.class,
                        "flip.cl",
                        "flip_3d",
                        parameters);
  }

  public static boolean flip(ClearCLIJ clij,
                             ClearCLImage src,
                             ClearCLImage dst,
                             boolean flipx,
                             boolean flipy)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("flipx", flipx ? 1 : 0);
    parameters.put("flipy", flipy ? 1 : 0);
    return clij.execute(Kernels.class,
                        "flip.cl",
                        "flip_2d",
                        parameters);
  }

  public static boolean flip(ClearCLIJ clij,
                             ClearCLBuffer src,
                             ClearCLBuffer dst,
                             boolean flipx,
                             boolean flipy,
                             boolean flipz)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("flipx", flipx ? 1 : 0);
    parameters.put("flipy", flipy ? 1 : 0);
    parameters.put("flipz", flipz ? 1 : 0);
    return clij.execute(Kernels.class,
                        "flip.cl",
                        "flip_3d",
                        parameters);
  }

  public static boolean flip(ClearCLIJ clij,
                             ClearCLBuffer src,
                             ClearCLBuffer dst,
                             boolean flipx,
                             boolean flipy)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    parameters.put("flipx", flipx ? 1 : 0);
    parameters.put("flipy", flipy ? 1 : 0);
    return clij.execute(Kernels.class,
                        "flip.cl",
                        "flip_2d",
                        parameters);
  }

  public static boolean invert(ClearCLIJ clij,
                               ClearCLImage input3d,
                               ClearCLImage output3d)
  {
    return multiplyScalar(clij, input3d, output3d, -1);
  }

  public static boolean invertBinary(ClearCLIJ clij,
                                     ClearCLImage src,
                                     ClearCLImage dst)
  {
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("src", src);
    parameters.put("dst", dst);
    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (copy)");
      return false;
    }

    return clij.execute(Kernels.class,
                        "binaryProcessing.cl",
                        "invert_" + src.getDimension() + "d",
                        parameters);
  }

  public static boolean localThreshold(ClearCLIJ clij,
                                  ClearCLImage src,
                                  ClearCLImage dst,
                                  ClearCLImage threshold)
  {
    HashMap<String, Object> lParameters = new HashMap<>();

    lParameters.clear();
    lParameters.put("local_threshold", threshold);
    lParameters.put("src", src);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
              "Error: number of dimensions don't match! (addScalar)");
      return false;
    }

    return clij.execute(Kernels.class,
            "thresholding.cl",
            "apply_local_threshold_" + src.getDimension() + "d",
            lParameters);
  }


  public static boolean mask(ClearCLIJ pCLIJ,
                             ClearCLImage src,
                             ClearCLImage mask,
                             ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("mask", mask);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (mask)");
      return false;
    }
    return pCLIJ.execute(Kernels.class,
                         "mask.cl",
                         "mask_" + src.getDimension() + "d",
                         lParameters);
  }

  public static boolean maskStackWithPlane(ClearCLIJ pCLIJ,
                                           ClearCLImage src,
                                           ClearCLImage mask,
                                           ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("mask", mask);
    lParameters.put("dst", dst);

    return pCLIJ.execute(Kernels.class,
                         "mask.cl",
                         "maskStackWithPlane",
                         lParameters);
  }

  public static boolean maxPixelwise(ClearCLIJ pCLIJ,
                                     ClearCLImage src,
                                     ClearCLImage src1,
                                     ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("src1", src1);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(),
            src1.getDimension(),
            dst.getDimension()))
    {
      System.out.println(
              "Error: number of dimensions don't match! (addPixelwise)");
      return false;
    }
    return pCLIJ.execute(Kernels.class,
            "math.cl",
            "maxPixelwise_" + src.getDimension() + "d",
            lParameters);
  }


  public static boolean maxProjection(ClearCLIJ pCLIJ,
                                      ClearCLImage src,
                                      ClearCLImage dst_max)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("dst_max", dst_max);

    pCLIJ.execute(Kernels.class,
                  "projections.cl",
                  "max_project_3d_2d",
                  lParameters);

    return true;
  }


  public static boolean maxProjection(ClearCLIJ pCLIJ,
                                      ClearCLImage src,
                                      ClearCLImage dst_max,
                                      int projectedDimensionX,
                                      int projectedDimensionY,
                                      int projectedDimension)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("dst_max", dst_max);
    lParameters.put("projection_x", projectedDimensionX);
    lParameters.put("projection_y", projectedDimensionY);
    lParameters.put("projection_dim", projectedDimension);

    pCLIJ.execute(Kernels.class,
                  "projections.cl",
                  "max_project_dim_select_3d_2d",
                  lParameters);

    return true;
  }



  public static boolean multiplyPixelwise(ClearCLIJ pCLIJ,
                                          ClearCLImage src,
                                          ClearCLImage src1,
                                          ClearCLImage dst)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("src1", src1);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(),
                         src1.getDimension(),
                         dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (addScalar)");
      return false;
    }

    return pCLIJ.execute(Kernels.class,
                         "math.cl",
                         "multiplyPixelwise_"
                         + src.getDimension()
                         + "d",
                         lParameters);
  }

  public static boolean multiplyScalar(ClearCLIJ pCLIJ,
                                       ClearCLImage src,
                                       ClearCLImage dst,
                                       float scalar)
  {
    HashMap<String, Object> lParameters = new HashMap<>();
    lParameters.put("src", src);
    lParameters.put("scalar", scalar);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (addScalar)");
      return false;
    }
    return pCLIJ.execute(Kernels.class,
                         "math.cl",
                         "multiplyScalar_" + src.getDimension() + "d",
                         lParameters);
  }

  public static boolean multiplyStackWithPlane(ClearCLIJ clij,
                                               ClearCLImage input3d,
                                               ClearCLImage input2d,
                                               ClearCLImage output3d)
  {
    HashMap<String, Object> lParameters = new HashMap<>();

    lParameters.clear();
    lParameters.put("src", input3d);
    lParameters.put("src1", input2d);
    lParameters.put("dst", output3d);
    return clij.execute(Kernels.class,
                        "math.cl",
                        "multiplyStackWithPlanePixelwise",
                        lParameters);
  }

  public static boolean set(ClearCLIJ clij,
                            ClearCLImage clImage,
                            float value)
  {
    HashMap<String, Object> lParameters = new HashMap<>();

    lParameters.clear();
    lParameters.put("dst", clImage);
    lParameters.put("value", value);

    return clij.execute(Kernels.class,
                        "set.cl",
                        "set_" + clImage.getDimension() + "d",
                        lParameters);
  }

  public static boolean splitStack(ClearCLIJ clij, ClearCLImage clImageIn, ClearCLImage... clImagesOut) {
    if (clImagesOut.length > 12) {
      System.out.println("Error: splitStack does not support more than 5 stacks.");
      return false;
    }
    if (clImagesOut.length == 1) {
      return copy(clij, clImageIn, clImagesOut[0]);
    }
    if (clImagesOut.length == 0) {
      System.out.println("Error: splitstack didn't get any output images.");
      return false;
    }

    HashMap<String, Object> lParameters = new HashMap<>();

    lParameters.clear();
    lParameters.put("src", clImageIn);
    for (int i = 0; i < clImagesOut.length; i++) {
      lParameters.put("dst" + i, clImagesOut[i]);
    }

    return clij.execute(Kernels.class,
            "stacksplitting.cl",
            "split_" + clImagesOut.length + "_stacks",
            lParameters);
  }

  public static double sumPixels(ClearCLIJ clij, ClearCLImage clImage)
  {
    ClearCLImage clReducedImage = clImage;
    if (clImage.getDimension() == 3)
    {
      clReducedImage =
          clij.createCLImage(new long[] { clImage.getWidth(),
                                          clImage.getHeight() },
                             clImage.getChannelDataType());

      HashMap<String, Object> parameters = new HashMap<>();
      parameters.put("src", clImage);
      parameters.put("dst", clReducedImage);
      clij.execute(Kernels.class,
                   "projections.cl",
                   "sum_project_3d_2d",
                   parameters);
    }

    RandomAccessibleInterval
        rai =
        clij.converter(clReducedImage).getRandomAccessibleInterval();
    Cursor cursor = Views.iterable(rai).cursor();
    float sum = 0;
    while (cursor.hasNext())
    {
      sum += ((RealType) cursor.next()).getRealFloat();
    }

    clReducedImage.close();
    return sum;
  }

  public static boolean tenengradFusion(ClearCLIJ clij, ClearCLImage clImageOut, float[] blurSigmas, ClearCLImage... clImagesIn) {
    if (clImagesIn.length > 5) {
      System.out.println("Error: tenengradFusion does not support more than 5 stacks.");
      return false;
    }
    if (clImagesIn.length == 1) {
      return copy(clij, clImagesIn[0], clImageOut);
    }
    if (clImagesIn.length == 0) {
      System.out.println("Error: tenengradFusion didn't get any output images.");
      return false;
    }
    if (!clImagesIn[0].isFloat()) {
      System.out.println("Warning: tenengradFusion may only work on float images!");
    }

    HashMap<String, Object> lFusionParameters = new HashMap<>();

    ClearCLImage temporaryImage = clij.createCLImage(clImagesIn[0]);

    ClearCLImage[] temporaryImages = new ClearCLImage[clImagesIn.length];
    for (int i = 0; i < clImagesIn.length; i++) {
      HashMap<String, Object> lParameters = new HashMap<>();
      temporaryImages[i] = clij.createCLImage(clImagesIn[i]);
      lParameters.put("src", clImagesIn[i]);
      lParameters.put("dst", temporaryImage);

      clij.execute(Kernels.class,
              "tenengradFusion.cl",
              "tenengrad_weight_unnormalized",
              lParameters);

      //clij.show(temporaryImage, "temp before gauss");

      blurSeparable(clij, temporaryImage, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);

      //clij.show(temporaryImages[i], "temp after gauss");

      lFusionParameters.put("src" + i, clImagesIn[i]);
      lFusionParameters.put("weight" + i, temporaryImages[i]);
    }

    lFusionParameters.put("dst", clImageOut);
    lFusionParameters.put("factor", (int) (clImagesIn[0].getWidth() / temporaryImages[0].getWidth()));

    boolean success = clij.execute(Kernels.class,
            "tenengradFusion.cl",
            String.format("tenengrad_fusion_with_provided_weights_%d_images", clImagesIn.length),
            lFusionParameters);

    temporaryImage.close();
    for (int i = 0; i < temporaryImages.length; i++) {
      //clij.show(temporaryImages[i], "temp " + i);
      temporaryImages[i].close();
    }
    //clij.show(clImageOut, "tenengrad out ");

    System.out.println("clij " + clImagesIn.length);

    return success;
  }

  public static boolean threshold(ClearCLIJ clij,
                                  ClearCLImage src,
                                  ClearCLImage dst,
                                  float threshold)
  {
    HashMap<String, Object> lParameters = new HashMap<>();

    lParameters.clear();
    lParameters.put("threshold", threshold);
    lParameters.put("src", src);
    lParameters.put("dst", dst);

    if (!checkDimensions(src.getDimension(), dst.getDimension()))
    {
      System.out.println(
          "Error: number of dimensions don't match! (addScalar)");
      return false;
    }

    return clij.execute(Kernels.class,
                        "thresholding.cl",
                        "apply_threshold_" + src.getDimension() + "d",
                        lParameters);
  }

  private static boolean checkDimensions(long... numberOfDimensions)
  {
    for (int i = 0; i < numberOfDimensions.length - 1; i++)
    {
      if (!(numberOfDimensions[i] == numberOfDimensions[i + 1]))
      {
        return false;
      }
    }
    return true;
  }

}

